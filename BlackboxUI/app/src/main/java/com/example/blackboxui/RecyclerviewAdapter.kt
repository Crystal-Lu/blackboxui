package com.example.blackboxui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return CardViewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.event_card, p0, false)
        )
    }
    override fun getItemCount(): Int {
        val userList : ArrayList<Int> = ArrayList<Int>()
        return 10
    }
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

//        p0.name?.text = userList[p1].name
//        p0.count?.text = userList[p1].count.toString()
    }
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name = itemView.findViewById<TextView>(R.id.tvName)
//        val count = itemView.findViewById<TextView>(R.id.tvCount)

    }
    class CardViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        //val taskText: TextView = itemView.text

//        fun bind(task: Task){
//            taskText.setText(task.text)
//
//        }

    }