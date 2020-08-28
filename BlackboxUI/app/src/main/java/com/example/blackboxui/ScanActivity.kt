package com.example.blackboxui

import android.animation.Animator
import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.NfcA
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_scan.*
import java.nio.charset.StandardCharsets.US_ASCII


class ScanActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null


    private fun readAnimation(){
        radar.setAnimation(R.raw.scanningpop)
        radar.playAnimation()
        radar.repeatCount=0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        val backButton = backButton

        //Toast.makeText(this,radar.width.toString(),Toast.LENGTH_SHORT).show()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        backButton.setOnClickListener { view ->
            escape()
        }

        testButton.setOnClickListener { view ->


            readAnimation()


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

    override fun onResume() {
        super.onResume()


        var tagDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        var ndefDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        var techDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        var nfcIntentFilter =
                arrayOf(techDetected, tagDetected, ndefDetected)

        var pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP), 0


        )
        nfcAdapter?.enableForegroundDispatch(
                this,
                pendingIntent,
                nfcIntentFilter,
                null
        )


        tagOps(intent)

    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)

//        saveData(name_field)
//        saveData(email_field)
//        saveData(phone_field)
    }

    fun tagOps(intent: Intent){



        var action : String? = intent.getAction()
        if(action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) ||
                action.equals(NfcAdapter.ACTION_TECH_DISCOVERED) ){
            //val tag = intent.getParcelableArrayExtra<Tag>(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val tag = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            //val msg : NdefMessage

            if (tag != null) {

                for (i in 0 until tag.size) {


                    val msg  = tag[i] as NdefMessage
                    if (msg.records != null) {
                        val rec : NdefRecord = msg.records[i]
                        //Toast.makeText(applicationContext, rec.type.toString(), Toast.LENGTH_SHORT).show()

                        val path = "blackbox:nfcapp"
                        if (rec.type!!.contentEquals(path.toByteArray())) {

                            Toast.makeText(applicationContext, String(rec.getPayload(), US_ASCII), Toast.LENGTH_SHORT).show()


                        }
                        //secretMessage.setText(new String(rec.getPayload(), US_ASCII));
                    }
                }

                }


//
//            if (tag != null) {
//                val messages = arrayOfNulls<NdefMessage>(tag.size)
//                for (i in 0 until tag.size) {
//                    messages[i] : NdefMessage = ""
//
//                    com.sun.org.apache.xml.internal.serializer.utils.Utils.messages[i] = tag[i]
//                }
//                }
//                }
//            }

            //patchTag(tag)
            //Toast.makeText(applicationContext, "nfc recieved", Toast.LENGTH_SHORT).show()
            readAnimation()
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            //tag?.let { readFromNFC(it, intent) }

        }


    }

    override fun onNewIntent(intent: Intent) {

        super.onNewIntent(intent)
        this.setIntent(intent)


        tagOps(intent)
//        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
//        patchTag(tag)
//
//        Toast.makeText(applicationContext, "nfc recieved", Toast.LENGTH_LONG).show()
////
////        val intent = Intent(this,ScanActivity::class.java)
////        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
//        tag?.let { readFromNFC(it, intent) }
    }

    fun escape(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

    }


}