package com.example.blackboxui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contacts_list.view.*
import kotlinx.android.synthetic.main.event_card.view.*

class RecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context : Context
    var events: ArrayList<Pair<String, String>> = Repository.party

    fun addEvents() {
//        if (events.size > 10){return}
//        events.add(Pair("Martha's Litfest", "7/31/20"))
//        events.add(Pair("Ryan's Rager", "8/10/20"))
//        Toast.makeText(context, "hello",Toast.LENGTH_SHORT).show()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
//        addEvents()
//        Toast.makeText(context, "hello",Toast.LENGTH_SHORT).show()
        context = p0.context
        return CardViewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.event_card, p0, false)
        )
    }

    override fun getItemCount(): Int {
        val userList: ArrayList<Int> = ArrayList<Int>()
        return events.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
//        Toast.makeText(context, "hello",Toast.LENGTH_SHORT).show()
        when (holder) {
            is CardViewHolder -> holder.bind(events[p1])
        }
//        p0.name?.text = userList[p1].name
//        p0.count?.text = userList[p1].count.toString()
    }
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name = itemView.findViewById<TextView>(R.id.tvName)
//        val count = itemView.findViewById<TextView>(R.id.tvCount)


    class CardViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val v0 = itemView
        fun bind(pair: Pair<String, String>) {
            v0.Name.text = pair.first
            v0.Date.text = pair.second
//            v0.Name.text = "hello"
//            v0.Date.text = "rip"

        }

    }
}