package com.example.blackboxui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.blackboxui.ui.dashboard.DashboardFragment
import com.example.blackboxui.ui.home.HomeFragment
import com.example.blackboxui.ui.notifications.NotificationsFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contacts_card.*

class MainActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("test")
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val scannerButton : Button = scanButton

        //init()

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(setOf(
        //        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        scannerButton.setOnClickListener{ view ->
            //Toast.makeText(this,countDownButton.isChecked.toString(),Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ScanActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

        }

    }
    override fun onResume() {
        super.onResume()

        val user = ArrayList<Pair<String,String>>()
        val party = ArrayList<(Pair<String,String>)>()
        val update = ArrayList<Pair<String,String>>()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(orbs in dataSnapshot.children){
                    for(dates in orbs.children){
                        for(data in dates.children){
                            user.add(Pair(data.value.toString(),data.key.toString()))
                            party.add(Pair(orbs.value.toString(),dates.value.toString()))
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
    /*
    private fun init() {

        view_pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {

                //return ThisWeekFragment.newInstance()

                return when(position) {

                    0 -> HomeFragment.newInstance()
                    1 -> DashboardFragment.newInstance()
                    else -> NotificationsFragment.newInstance()

                }

            }
        }
        /*TabLayoutMediator(tabs, view_pager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.to_do)
                1 -> getString(R.string.this_week)
                else -> getString(R.string.today)
            }
        }.attach()

         */

    }*/
}
