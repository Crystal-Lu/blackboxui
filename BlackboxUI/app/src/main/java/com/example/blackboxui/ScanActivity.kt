package com.example.blackboxui

import android.animation.Animator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {


    fun escape(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        val backButton = backButton
        //Toast.makeText(this,radar.width.toString(),Toast.LENGTH_SHORT).show()

        backButton.setOnClickListener { view ->
            //Toast.makeText(this,countDownButton.isChecked.toString(),Toast.LENGTH_SHORT).show()
            escape()
        }

        testButton.setOnClickListener { view ->

            radar.setAnimation(R.raw.scanningpop)
            radar.playAnimation()
            radar.repeatCount=0


            searchingText.setText("Joined \"Martha\'s Litfest\"")
            //Toast.makeText(this,radar.width.toString(),Toast.LENGTH_SHORT).show()
        }

        radar.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (radar.repeatCount == 0) {
                    escape()
                }
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }
        })



    }

}