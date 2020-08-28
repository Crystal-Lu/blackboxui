package com.example.blackboxui

import android.animation.Animator
import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
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
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            patchTag(tag)
            Toast.makeText(applicationContext, "nfc recieved", Toast.LENGTH_SHORT).show()
            readAnimation()
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            tag?.let { readFromNFC(it, intent) }

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

    fun patchTag(oTag: Tag?): Tag? {
        if (oTag == null) return null
        val sTechList = oTag.techList
        val oParcel: Parcel
        val nParcel: Parcel
        oParcel = Parcel.obtain()
        oTag.writeToParcel(oParcel, 0)
        oParcel.setDataPosition(0)
        val len = oParcel.readInt()
        var id: ByteArray? = null
        if (len >= 0) {
            id = ByteArray(len)
            oParcel.readByteArray(id)
        }
        val oTechList = IntArray(oParcel.readInt())
        oParcel.readIntArray(oTechList)
        val oTechExtras = oParcel.createTypedArray(Bundle.CREATOR)
        val serviceHandle = oParcel.readInt()
        val isMock = oParcel.readInt()
        val tagService: IBinder?
        tagService = if (isMock == 0) {
            oParcel.readStrongBinder()
        } else {
            null
        }
        oParcel.recycle()
        var nfca_idx = -1
        var mc_idx = -1
        for (idx in sTechList.indices) {
            if (sTechList[idx] === NfcA::class.java.name) {
                nfca_idx = idx
            } else if (sTechList[idx] === MifareClassic::class.java.name) {
                mc_idx = idx
            }
        }
        if (nfca_idx >= 0 && mc_idx >= 0 && oTechExtras!![mc_idx] == null) {
            oTechExtras[mc_idx] = oTechExtras[nfca_idx]
        } else {
            return oTag
        }
        nParcel = Parcel.obtain()
        nParcel.writeInt(id!!.size)
        nParcel.writeByteArray(id)
        nParcel.writeInt(oTechList.size)
        nParcel.writeIntArray(oTechList)
        nParcel.writeTypedArray(oTechExtras, 0)
        nParcel.writeInt(serviceHandle)
        nParcel.writeInt(isMock)
        if (isMock == 0) {
            nParcel.writeStrongBinder(tagService)
        }
        nParcel.setDataPosition(0)
        val nTag = Tag.CREATOR.createFromParcel(nParcel)
        nParcel.recycle()
        return nTag
    }


    private fun readFromNFC(tag: Tag, intent: Intent) {


//        indiv = Indiv()



        /*
        val name = findViewById<EditText>(com.google.firebase.database.R.id.name_field).text.toString()
        val email = findViewById<EditText>(com.google.firebase.database.R.id.email_field).text.toString()
        val phone = findViewById<EditText>(com.google.firebase.database.R.id.phone_field).text.toString()



        if (name == "" || email == "" || phone == "") {
            Toast.makeText(applicationContext, "Name, email, or phone fields are empty", Toast.LENGTH_LONG).show()
        } else if (!indiv.matchEmail(email)) {
            Toast.makeText(applicationContext, "Enter valid Cornell email", Toast.LENGTH_LONG).show()
        } else if (!indiv.matchNumber(phone)) {
            Toast.makeText(applicationContext, "Enter valid phone number", Toast.LENGTH_LONG).show()
        } else {


            when (userState) {

                "HOME" -> {
                    findViewById<View>(com.google.firebase.database.R.id.pane).setBackgroundColor(Color.parseColor("#FFFFFF"))
                    userState = "CLEARED"
                }
                else -> {
                    findViewById<View>(com.google.firebase.database.R.id.pane).setBackgroundColor(Color.parseColor("#FF2FFF"))
                    userState = "HOME"
                }
            }

            indiv.setName(name)
            indiv.setEmail(email)
            indiv.setPhoneNumber(phone)
            //indiv.setTimestamp()
            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.BASIC_ISO_DATE
            val formatted = current.format(formatter)
            database.child("party-2").child("$formatted").child(indiv.getName()).setValue(indiv)

         */
    }

}