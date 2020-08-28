package com.example.blackboxui.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blackboxui.R
import com.example.blackboxui.RecyclerviewAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    val events : ArrayList<Pair<String,String>> = ArrayList()

    fun addEvents() {
        events.add(Pair("Martha's Litfest", "7/31/20"))
        events.add(Pair("Ryan's Rager", "8/10/20"))
    }


    private lateinit var homeViewModel: HomeViewModel


    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        //task_card.card

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        retainInstance=true

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    private fun initRecyclerView(){

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            //val topSpacingDecorator = TopSpacingItemDecoration(30)
            //addItemDecoration(topSpacingDecorator)
            var recycleAdapter = RecyclerviewAdapter()
            recycleAdapter.addEvents()
            adapter = recycleAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }
}
