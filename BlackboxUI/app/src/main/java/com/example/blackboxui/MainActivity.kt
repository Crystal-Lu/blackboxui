package com.example.blackboxui

import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    //private lateinit var database: DatabaseReference
    var user = ArrayList<Pair<String, String>>()
    var party = ArrayList<(Pair<String, String>)>()
    var update = ArrayList<Pair<String, String>>()

    // NFC

    private var nfcAdapter: NfcAdapter? = null

    var notificationsCardState = 0

    var hostPayload = ""

    var navControllerState = ""

    lateinit var navController: NavController

    lateinit var mintent: Intent

    //

    val data: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("blackbox")
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val scannerButton: Button = scanButton

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        scannerButton.setOnClickListener { view ->
            val intent = Intent(this, ScanActivity::class.java)
            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val phoneString = sharedPreferences.getString("Phone", null)
            intent.putExtra("Phone", phoneString)
            val nameString = sharedPreferences.getString("Name", null)
            intent.putExtra("Name", nameString)

            var name: String? = sharedPreferences.getString("Name", null)
            val phone: String? = sharedPreferences.getString("Phone", null)

            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())


        }

//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                //Log.d("testing3", dataSnapshot.children.toString())
//                for (orbs in dataSnapshot.children) {
//                    //Log.d("testing3", orbs.toString())
//                    for (dates in orbs.children) {
//
//
//                        Repository.party.add(Pair(orbs.key.toString(), dates.key.toString()))
//                        for (data in dates.children) {
//                            Repository.users.add(Pair(data.value.toString(), data.key.toString()))
//                            Repository.party2.add(Pair(Pair(orbs.key.toString(), dates.key.toString()),Pair(data.value.toString(), data.key.toString())))
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                println("The read failed: " + databaseError.code)
//            }
//        })

    }

    override fun onResume() {
        super.onResume()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("testing3", dataSnapshot.children.toString())
                for (orbs in dataSnapshot.children) {
                    //Log.d("testing3", orbs.toString())
                    for (dates in orbs.children) {

                        Log.d("Ham", Repository.party.indexOf(Pair(orbs.key.toString(), dates.key.toString())).toString())
                        if (Repository.party.indexOf(Pair(orbs.key.toString(), dates.key.toString()))==-1 ) {


                            Repository.party.add(Pair(orbs.key.toString(), dates.key.toString()))
                            for (data in dates.children) {
                                Repository.users.add(Pair(data.value.toString(), data.key.toString()))
                                Repository.party2.add(Pair(Pair(orbs.key.toString(), dates.key.toString()),Pair(data.value.toString(), data.key.toString())))
                        }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        Log.d("testing", user.toString())
        Log.d("testing2", party.toString())

        var tagDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        var ndefDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        var techDetected : IntentFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        var nfcIntentFilter =
                arrayOf(techDetected, tagDetected, ndefDetected)

        var pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        nfcAdapter?.enableForegroundDispatch(
                this,
                pendingIntent,
                nfcIntentFilter,
                null
        )

    }



    override fun onPause() {
        super.onPause()

        nfcAdapter?.disableForegroundDispatch(this)
    }

    var fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        patchTag(tag)
        tag?.let { readFromNFC(it, intent) }
    }
    private fun readFromNFC(tag: Tag, intent: Intent) {
            tagOps(intent)
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


    fun tagOps(intent: Intent){
        Log.d("Ham","tagops")
        var action : String? = intent.getAction()
        if(action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) ||
                action.equals(NfcAdapter.ACTION_TECH_DISCOVERED) || action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)

        ){
            //val tag : Tag? = intent.getParcelableArrayExtra<Tag?>(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val tag = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            //val msg : NdefMessage

                            if (navController.currentDestination?.label == "Notifications") {

                                if (notificationsCardState == 1) {
                                    //Toast.makeText(this, notificationsCardState.toString(), Toast.LENGTH_SHORT).show()
                                    hostPayload = scanTagActiveField.text.toString()
                                    if (hostPayload == "") {
                                        Toast.makeText(this, "Fill in group name first", Toast.LENGTH_SHORT).show()

                                    } else {
                                        createNFCMessage(hostPayload,intent)
                                        Toast.makeText(this, "Success! $hostPayload is registered", Toast.LENGTH_SHORT).show()

                                    }
                                } else {
                                    Toast.makeText(this, "How will you host your party?", Toast.LENGTH_SHORT).show()
                                }
                            } else {

                                Toast.makeText(this, "Please click \"Scan\" to continue scanning", Toast.LENGTH_SHORT).show()
                            }


                        }
                    }



    fun createNFCMessage(payload: String, intent: Intent?) : Boolean {

        val pathPrefix = "blackbox:nfcapp"

        Log.d("NFC", "attempted to write")
        val nfcRecord = NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, pathPrefix.toByteArray(), ByteArray(0), payload.toByteArray())
        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent?.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return  writeMessageToTag(nfcMessage, tag)
        }
        return false

    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()
                if (it.maxSize < nfcMessage.toByteArray().size) {
                    //Message to large to write to NFC tag
                    return false
                }
                if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    //Message is written to tag
                    return true
                } else {
                    //NFC tag is read-only
                    return false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    //The data is written to the tag
                    return true
                } catch (e: IOException) {
                    //Failed to format tag
                    return false
                }
            }
            //NDEF is not supported
            return false

        } catch (e: Exception) {
            //Write operation has failed
        }
        return false
    }
}









