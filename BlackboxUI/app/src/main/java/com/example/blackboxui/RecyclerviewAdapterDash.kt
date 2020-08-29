package com.example.blackboxui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blackboxui.Repository.party
import com.example.blackboxui.Repository.party2
import com.example.blackboxui.Repository.users
import kotlinx.android.synthetic.main.contacts_card.view.*
import kotlinx.android.synthetic.main.event_card.view.Date


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
        return party.count()
    }

    override fun onBindViewHolder(p0: CardViewHolder, p1: Int) {



//        addContacts()


//        Log.d("Hotdog2", party[1].toString())
//        Log.d("Hotdog3", party[1].first)


        p0.recyclerView.layoutManager = LinearLayoutManager(context)

        var filteredList = arrayListOf<Pair<Pair<String, String>, Pair<String, String>>>()

        for (superpair in party2) {
            Log.d("superpair", "$party at $p1 with $contacts contacts")
            if (superpair.first.first ==
                    party[p1].first &&
                    superpair.first.second ==
                    party[p1].second) {
                filteredList.add(superpair)
            }

        }


        p0.recyclerView.adapter = RecyclerviewAdapterContact(filteredList, party[p1],context)

        when (p0){
            is CardViewHolder -> p0.bind(party[p1])
        }

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
            v0.contactHeader.text = pair.first
            v0.Date.text = pair.second
        }
//        fun bind(task: Task){
//            taskText.setText(task.text)
//
//        }

    }
}