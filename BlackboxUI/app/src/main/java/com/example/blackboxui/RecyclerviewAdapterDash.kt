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
import kotlinx.android.synthetic.main.event_card.view.*


class RecyclerviewAdapterDash : RecyclerView.Adapter<RecyclerviewAdapterDash.CardViewHolder>() {

    var contacts = Repository.users

    //TODO: create third data structure for past events, not sure how to do

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
        p0.recyclerView.adapter = RecyclerviewAdapterContact(contacts, context)

//        when (p0) {
//            is CardViewHolder ->
//            {
//                p0.bind(pastEvents[p1])
//                p0.recyclerView.layoutManager = LinearLayoutManager(context)
//                p0.recyclerView.adapter = RecyclerviewAdapterContact (contacts,context)
//            }
//        }

    }


    class CardViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val recyclerView : RecyclerView = itemView.findViewById(R.id.contactsList)
        //val taskText: TextView = itemView.text

        val v0 = itemView
        fun bind(pair: Pair<String, String>) {
            v0.Name.text = pair.first
            v0.Date.text = pair.second
        }
//        fun bind(task: Task){
//            taskText.setText(task.text)
//
//        }

    }
}