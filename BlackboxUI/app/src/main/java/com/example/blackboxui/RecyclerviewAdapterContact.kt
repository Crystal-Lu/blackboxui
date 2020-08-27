package com.example.blackboxui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.contacts_list.view.*


class RecyclerviewAdapterContact : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return CardViewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.contacts_list, p0, false)
        )
    }

    override fun getItemCount(): Int {
        val userList: ArrayList<Int> = ArrayList<Int>()
        return 10
    }

//    class ContactViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val contactslist = itemView.Contact
//        fun bind(task: Task){
//            contactslist.setText(task.text)
//        }
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemContact = holder
//        holder.contactsList.text = items.get(position)
    }
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name = itemView.findViewById<TextView>(R.id.tvName)
//        val count = itemView.findViewById<TextView>(R.id.tvCount)

    class CardViewHolder constructor(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val contactsList = view.Person
        fun bind(card: )
        //val taskText: TextView = itemView.text
//        fun bind(task: Task){
//            taskText.setText(task.text)
//
//        }

    }
}