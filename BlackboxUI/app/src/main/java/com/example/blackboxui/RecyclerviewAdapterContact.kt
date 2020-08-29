package com.example.blackboxui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contacts_list.view.*



class RecyclerviewAdapterContact(val filteredList: ArrayList<Pair<Pair<String, String>, Pair<String, String>>>, val matchPair: Pair<String, String>,val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.contacts_list, p0, false)

        return CardViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
//        val userList: ArrayList<Int> = ArrayList<Int>()
//        return 10
        return filteredList.size
    }

//    class ContactViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val contactslist = itemView.Contact
//        fun bind(task: Task){
//            contactslist.setText(task.text)
//        }
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder){
            is CardViewHolder -> holder.bind(filteredList[position].second)
        }
//        Contact.text = itemsContactList[position].first
//        holder.Person = itemsContactList[position].second

    }
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name = itemView.findViewById<TextView>(R.id.tvName)
//        val count = itemView.findViewById<TextView>(R.id.tvCount)

    class CardViewHolder constructor(
        view: View
    ) : RecyclerView.ViewHolder(view) {
//        val contactsList = view.Person
//        fun bind(card: )
        //val taskText: TextView = itemView.text
        val v0 = view
        fun bind(pair : Pair<String,String>){
            v0.Contact.text = pair.first
            v0.PhoneNumber.text = pair.second

        }

    }
}