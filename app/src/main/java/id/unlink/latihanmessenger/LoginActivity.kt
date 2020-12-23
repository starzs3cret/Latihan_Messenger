package id.unlink.latihanmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.unlink.latihanmessenger.databinding.ActivityLoginBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: id.unlink.latihanmessenger.databinding.ActivityLoginBinding
    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var mAuth:FirebaseAuth

    private val RC_SIGN_IN = 9001
    private val TAG = "LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        FirebaseApp.initializeApp(this)
        mAuth = Firebase.auth
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mSignInClient = GoogleSignIn.getClient(this,gso)


        binding.btSignin.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser?.reload()!=null){
            startActivity(Intent(this,ListChatActivity::class.java))
            finish()
        }
    }

    private fun signIn() {
        val signInIntent= mSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account:GoogleSignInAccount = task.getResult(ApiException::class.java)
                lanjutlogin(account)
            } catch(e:Exception){
                Log.e(TAG, "onActivityResult: gagal login", e)
            }
        }
    }

    private fun lanjutlogin(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    //
                    createDatabase(it.user!!)
                    startActivity(Intent(this,ListChatActivity::class.java))
                    finish()
                }
    }

    private fun createDatabase(currentUser: FirebaseUser) {
        val map = HashMap<String,Any>()
        val now = Calendar.getInstance().timeInMillis
        map["users"] = currentUser.uid
        map["type"] = "student"
        map["created"] = now
        map["contact"] = listOf("") // uid
        map["name"] = currentUser.displayName.toString()
        map["email"] = currentUser.email.toString()
        Firebase.firestore.collection("users").document(currentUser.uid)
                .set(map).addOnSuccessListener {

                }.addOnFailureListener {

                }
    }

}