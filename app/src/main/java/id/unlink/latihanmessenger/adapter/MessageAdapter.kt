package id.unlink.latihanmessenger.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.unlink.latihanmessenger.R
import id.unlink.latihanmessenger.model.MessagE

class MessageAdapter(private val listMessage: ArrayList<MessagE>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(list: ArrayList<MessagE>) {
        listMessage.clear()
        listMessage.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newHolder = holder as MessageViewHolder
        newHolder.bind(listMessage[position], position)
    }

    override fun getItemCount(): Int = listMessage.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_message, parent, false)
        )
    }
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message:MessagE, position: Int) {
            with(itemView) {
                Log.d("", "bind: ${message.map} ")
                findViewById<TextView>(R.id.messageTextView).text = message.text.toString()
                findViewById<TextView>(R.id.messageTextView).visibility = TextView.VISIBLE
                findViewById<ImageView>(R.id.messageImageView).visibility = ImageView.GONE
                findViewById<TextView>(R.id.messengerTextView).text = message.name.toString()
                Glide.with(itemView.context)
                    .load(message.photoUrl)
                    .into(findViewById(R.id.messengerImageView))



            }
        }
    }
}