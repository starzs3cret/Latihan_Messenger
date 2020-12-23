package id.unlink.latihanmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.unlink.latihanmessenger.adapter.MessageAdapter
import id.unlink.latihanmessenger.databinding.ActivityTeskMessageBinding
import id.unlink.latihanmessenger.model.MessagE
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.log

class TeskMessageActivity : AppCompatActivity() {

    private lateinit var binding:ActivityTeskMessageBinding
    private lateinit var mAuth: FirebaseAuth
    private var list= ArrayList<MessagE>()
    private lateinit var ids:String
    private lateinit var adapter:MessageAdapter
    private lateinit var child:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent
        binding = DataBindingUtil
            .setContentView(this,R.layout.activity_tesk_message)

        mAuth=Firebase.auth
        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        initData(bundle)
    }
    val TAG= "TAG"
    private fun initDatas(data: Intent?) {
        val custom = "${data?.getStringExtra("from")}${data?.getStringExtra("to")}"


        val messagedb = Firebase.database.getReference("messages")
        messagedb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ${snapshot}")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        messagedb.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // disini aja
                Log.d(TAG, "onChildChanged: $snapshot")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sendMessage() {
        val text = binding.messageEditText.text.toString().trim()
        binding.messageEditText.text.clear()
        val now = Calendar.getInstance().timeInMillis
        val currentUser=mAuth.currentUser!!
        val map = HashMap<String,Any>()
        map["id"] = UUID.randomUUID().toString()
        map["user"] = currentUser.uid
        map["name"] = currentUser.displayName.toString()
        map["text"] = text
        map["photoUrl"] = currentUser.photoUrl.toString()
        map["imgUrl"] = ""
        map["fileUrl"] = ""
        map["time"] = now


        val messagedb = Firebase.firestore.collection("message")
        messagedb.document(ids).collection("messages").add(map)
        //val messagebase = Firebase.database.getReference("messages").push()
        //messagebase.updateChildren(map)
    }

    private fun initData(data : Intent) {
        val a = "${data.getStringExtra("from")}${data.getStringExtra("to")}"
        val b = "${data.getStringExtra("to")}${data.getStringExtra("from")}"
        val messagedb = Firebase.firestore.collection("message")
        val TAG = "-----"
        var hasrun=false
        messagedb
            //.whereArrayContainsAny("contact",listOf(data.getStringExtra("from"),data.getStringExtra("to")))
            .whereArrayContainsAny("contact", listOf(a,b))
            .get().addOnSuccessListener { qu->
                if (qu.isEmpty){
                    Log.d(TAG, "initData: empty")
                    val map = HashMap<String,Any>()
                    //map["id_chat"] =
                    Log.d(TAG, "initData: from ${data.getStringExtra("from")}\n to ${data.getStringExtra("to")}")
                    map["contact"] = listOf(a,b)
                    map["message"] = listOf("")
                    messagedb.add(map).addOnSuccessListener { ne->
                        ids =ne.id
                        map["id_chat"] = ids
                        messagedb.document(ids).set(map)
                        initData(data)
                    }
                } else {
                    ids =  qu.documents[0].id
                    val newdb =  messagedb.document(ids).collection("messages").orderBy("time",Query.Direction.DESCENDING)
                    Log.d(TAG, "initData: ${qu.documents}")

                    newdb.addSnapshotListener { value, error ->
                        if (value != null) {
                            if (value.isEmpty){

                            } else{
                                list.clear()
                                for (n in value.documents){
                                    //Log.d(TAG, "initData: listener ${n.data}")

                                    list.add(MessagE(n.data!!))

                                }
                                val adapter = MessageAdapter(list)
                                binding.messageRecyclerView.setHasFixedSize(true)
                                binding.messageRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true)
                                binding.messageRecyclerView.adapter = adapter
                                Log.d(TAG, "initData: size ${binding.messageRecyclerView.size}")

                            }
                        }
                    }
                }

            }





    }
}