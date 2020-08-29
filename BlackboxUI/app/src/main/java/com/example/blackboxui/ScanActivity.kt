package com.example.blackboxui

import android.animation.Animator
import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.NfcA
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieDrawable
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_scan.*
import java.nio.charset.StandardCharsets.US_ASCII
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ScanActivity : AppCompatActivity() {

    lateinit var mintent : Intent

    private var SnfcAdapter: NfcAdapter? = null

    val data: FirebaseDatabase = FirebaseDatabase.getInstance()
    val database : DatabaseReference = data.getReference("blackbox")
    private fun readAnimation(){
        radar.setAnimation(R.raw.scanningpop)
        radar.playAnimation()
        radar.repeatCount=0
    }

    var phone : String? = null
    var name : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        SnfcAdapter = NfcAdapter.getDefaultAdapter(this)



        val backButton = backButton




        backButton.setOnClickListener { view ->
            radar.setAnimation(R.raw.scanning)
            radar.playAnimation()
            radar.repeatCount = LottieDrawable.INFINITE
            searchingText.text = "Searching for tag..."
            finish()
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
                    radar.setAnimation(R.raw.scanning)
                    radar.playAnimation()
                    radar.repeatCount = LottieDrawable.INFINITE
                    searchingText.text = "Searching for tag..."
                    finish()
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

        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        name = sharedPreferences.getString("Name", null)
        phone = sharedPreferences.getString("Phone", null)

        //Toast.makeText(this, "$name + $phone",Toast.LENGTH_SHORT).show()


        var tagDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        var ndefDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        var techDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        var nfcIntentFilter =
                arrayOf(techDetected, tagDetected, ndefDetected)

        var pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION), 0


        )
        SnfcAdapter?.enableForegroundDispatch(
                this,
                pendingIntent,
                nfcIntentFilter,
                null
        )

    }

    override fun onPause() {
        super.onPause()
        SnfcAdapter?.disableForegroundDispatch(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun tagOps(intent: Intent){

        var action : String? = intent.getAction()
        if(action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) ||
                action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)|| action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)){
            val tag = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            if (tag != null) {

                for (i in 0 until tag.size) {


                    val msg  = tag[i] as NdefMessage
                    if (msg.records != null) {
                        val rec : NdefRecord = msg.records[i]
                        //Toast.makeText(applicationContext, rec.type.toString(), Toast.LENGTH_SHORT).show()

                        val path = "blackbox:nfcapp"
                        if (rec.type!!.contentEquals(path.toByteArray())) {


                            val party = String(rec.payload, US_ASCII)
                            searchingText.text = "Found \"$party\""
                            //database.child(party).child("asd").child("asd").setValue("asddw")
                            //Toast.makeText(applicationContext, name, Toast.LENGTH_LONG).show()

                            if (name == null || phone == null) {
                                Toast.makeText(applicationContext, "Name, or phone fields are empty. Go back to fix.", Toast.LENGTH_LONG).show()

                            } else {
                                //Toast.makeText(applicationContext, "Name: " + name + " Phone: " + phone, Toast.LENGTH_SHORT).show()
                                //indiv.setTimestamp()
                                val current = LocalDateTime.now()

                                val formatter = DateTimeFormatter.BASIC_ISO_DATE
                                val formatted = current.format(formatter)
                                database.child(party.toUpperCase()).child("$formatted").child(phone!!).setValue(name!!.toUpperCase())
                                readAnimation()
                            }


                        }
                    }
                }

                }

        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNewIntent(intent: Intent) {

        super.onNewIntent(intent)

        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        patchTag(tag)
        tag?.let { readFromNFC(it, intent) }

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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun readFromNFC(tag: Tag, intent: Intent) {
        tagOps(intent)


    }

}