package com.example.blackboxui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contacts_card.*



class RecyclerviewAdapterDash : RecyclerView.Adapter<RecyclerviewAdapterDash.CardViewHolder>() {

    var contacts = Repository.users
    lateinit var context : Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CardViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.contacts_card, p0, false) as View
        context = p0.context
        return CardViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        val userList: ArrayList<Int> = ArrayList<Int>()
        return 10
    }

    override fun onBindViewHolder(p0: CardViewHolder, p1: Int) {

        fun addContacts(){
            contacts.add(Pair("Chad Chadinson","888-8888"))
            contacts.add(Pair("Brad Bradinson","444-4444"))
        }

//        addContacts()
        p0.recyclerView.layoutManager = LinearLayoutManager(context)
//        p0.recyclerView.layoutManager = GridLayoutManager(context, 2)
        p0.recyclerView.adapter = RecyclerviewAdapterContact(contacts, context)
//        p0.name?.text = userList[p1].name
//        p0.count?.text = userList[p1].count.toString()
    }
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name = itemView.findViewById<TextView>(R.id.tvName)
//        val count = itemView.findViewById<TextView>(R.id.tvCount)

    class CardViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val recyclerView : RecyclerView = itemView.findViewById(R.id.contactsList)
        //val taskText: TextView = itemView.text

//        fun bind(task: Task){
//            taskText.setText(task.text)
//
//        }

    }
}