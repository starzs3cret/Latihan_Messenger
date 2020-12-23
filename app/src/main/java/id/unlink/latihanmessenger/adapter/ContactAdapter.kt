package id.unlink.latihanmessenger.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.unlink.latihanmessenger.R
import id.unlink.latihanmessenger.model.PersonA

class ContactAdapter(private val listPersona: ArrayList<PersonA>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // click listener


    private var OnItemClickCallback: OnItemCLickCallback? = null

    interface OnItemCLickCallback {
        fun onItemClicked(data: PersonA, position: Int)

    }

    fun setOnItemCLickCallback(onItemCLickCallback: OnItemCLickCallback) {
        this.OnItemClickCallback = onItemCLickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PersonaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_contact, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newHolder = holder as PersonaViewHolder
        newHolder.bind(listPersona[position], position)
    }

    override fun getItemCount(): Int = listPersona.size
    inner class PersonaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(personA: PersonA, position: Int) {
            with(itemView) {
                findViewById<TextView>(R.id.txName).text = personA.name.toString()
                itemView.findViewById<TextView>(R.id.txEmail).text = personA.email.toString()
                itemView.setOnClickListener{
                    OnItemClickCallback?.onItemClicked(
                        personA,position
                    )
                }

            }
        }
    }

}