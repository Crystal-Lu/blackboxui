package com.example.blackboxui.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blackboxui.MainActivity
import com.example.blackboxui.R
import com.example.blackboxui.RecyclerviewAdapter
import com.example.blackboxui.RecyclerviewAdapterDash
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.user_card.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    companion object {
        fun newInstance() = DashboardFragment()
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

        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    private fun initRecyclerView2(){

        dashRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            //val topSpacingDecorator = TopSpacingItemDecoration(30)
            //addItemDecoration(topSpacingDecorator)
            var recycleAdapter2 = RecyclerviewAdapterDash()
//            addDataSet()
            adapter = recycleAdapter2
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView2()
    }


}
