package id.unlink.latihanmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.unlink.latihanmessenger.adapter.ContactAdapter
import id.unlink.latihanmessenger.databinding.ActivityListChatBinding
import id.unlink.latihanmessenger.model.PersonA

class ListChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListChatBinding
    private lateinit var mAuth: FirebaseAuth
    val list = ArrayList<PersonA>()
    val TAG = "ListChat"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_chat)

        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser?.reload() ?: "0"

        loaddata()


    }

    private fun loaddata() {
        Firebase.firestore.collection("users")
            .get().addOnSuccessListener { doc ->
                for (item in doc) {


                    val person = PersonA(item.data)
                    if (person.users!=mAuth.currentUser!!.uid) list.add(person)


                }
                val listAdapter = ContactAdapter(list)
                setupRecycleView(listAdapter)

            }


    }


    private fun setupRecycleView(listAdapter: ContactAdapter) {
        binding.listUser.setHasFixedSize(true)
        binding.listUser.layoutManager = LinearLayoutManager(this)
        binding.listUser.adapter = listAdapter
        listAdapter.setOnItemCLickCallback(object : ContactAdapter.OnItemCLickCallback {
            override fun onItemClicked(data: PersonA, position: Int) {

                Log.d(TAG, "onItemClicked: data ${data.map["users"].toString()}")
                val intent = Intent(this@ListChatActivity, TeskMessageActivity::class.java)
                intent.putExtra("from", Firebase.auth.currentUser!!.uid)
                intent.putExtra("to", data.users.toString())
                Log.d(TAG, "onItemClicked: $intent")
                startActivity(
                    intent
                )
            }

        })


    }
}