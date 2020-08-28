package com.example.blackboxui.ui.notifications

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.blackboxui.MainActivity
import com.example.blackboxui.R
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.tagScanCard
import kotlinx.android.synthetic.main.user_card.*


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    var cardState = 0 // 0 means no card active, 1 means top card active, -1 means bottom card



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        //task_card.card
     }

    override fun onResume() {
        super.onResume()
        loadData(scanTagActiveField)
        loadData(phoneScanActiveField)
    }

    override fun onPause() {
        super.onPause()
        saveData(scanTagActiveField)
        saveData(phoneScanActiveField)
    }

    private fun saveData(et : EditText) {

        val insertedText = et.text.toString()

        val sharedPreferences = (activity as MainActivity).getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(et.id.toString(), insertedText)
        }.apply()
    }

    private fun loadData(et : EditText){
        val sharedPreferences = (activity as MainActivity).getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        try {
            val savedString = sharedPreferences.getString(et.id.toString(),null)
            et.setText(savedString)
        } finally {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //had to change background tint to background color in layout

        tagScanCard.setOnClickListener {view ->


            // BUG, unfocus text when change of card status

            when (cardState) {

                0 -> {tagScanCard.setCardBackgroundColor(ResourcesCompat.getColor(resources,R.color.colorAccent,null))
                    phoneScanCard.setCardBackgroundColor(ResourcesCompat.getColor(resources,R.color.translucentGrey,null))
                    phoneScanCard.alpha = 0.3F
                    cardState = 1
                    scanTagImage.visibility = View.GONE
                    scanTagText.visibility = View.GONE
                    scanTagActiveField.visibility = View.VISIBLE
                    scanTagActiveText.visibility = View.VISIBLE
                    phoneScanImage.visibility = View.GONE
                    phoneScanText.visibility = View.VISIBLE
                    phoneScanText.text = "Cancel"
                    phoneScanActiveField.visibility = View.INVISIBLE
                    phoneScanActiveText.visibility = View.GONE
                    phoneScanExit.visibility = View.VISIBLE
                    scanTagExit.visibility = View.GONE
                }
                1 -> {}
                -1 -> {phoneScanCard.setCardBackgroundColor(ResourcesCompat.getColor(resources,R.color.translucentGrey,null))
                    tagScanCard.alpha = 1F
                    cardState = 0
                    closeKeyboard()
                    //context?.let { hideKeyboardFrom(it,view) }
                    scanTagImage.visibility = View.VISIBLE
                    scanTagText.visibility = View.VISIBLE
                    scanTagActiveField.visibility = View.GONE
                    scanTagActiveText.visibility = View.GONE
                    phoneScanImage.visibility = View.VISIBLE
                    phoneScanText.visibility = View.VISIBLE
                    phoneScanText.text = "Host on this phone"
                    scanTagText.text = "Host with scan tag"
                    phoneScanActiveField.visibility = View.INVISIBLE
                    phoneScanActiveText.visibility = View.GONE
                    phoneScanExit.visibility = View.GONE
                    scanTagExit.visibility = View.GONE

                    scanTagActiveField.text = phoneScanActiveField.text
                }

            }

            //Toast.makeText(context,phoneScanActiveField.isFocused.toString(),Toast.LENGTH_SHORT).show()
            (activity as MainActivity).notificationsCardState = cardState


        }

        phoneScanCard.setOnClickListener() {view ->
            //Toast.makeText(this,countDownButton.isChecked.toString(),Toast.LENGTH_SHORT).show()

            when (cardState) {

                0 -> {phoneScanCard.setCardBackgroundColor(ResourcesCompat.getColor(resources,R.color.colorAccent,null))
                    tagScanCard.setCardBackgroundColor(ResourcesCompat.getColor(resources,R.color.translucentGrey,null))
                    tagScanCard.alpha = 0.3F
                    cardState = -1
                    phoneScanImage.visibility = View.GONE
                    phoneScanText.visibility = View.GONE
                    phoneScanActiveField.visibility = View.VISIBLE
                    phoneScanActiveText.visibility = (View.VISIBLE)
                    scanTagImage.visibility = View.GONE
                    scanTagText.visibility = View.VISIBLE
                    scanTagText.text = "Cancel"
                    scanTagActiveField.visibility = View.INVISIBLE
                    scanTagActiveText.visibility = View.GONE
                    phoneScanExit.visibility = View.GONE
                    scanTagExit.visibility = View.VISIBLE
                }
                -1 -> {}
                1 -> {tagScanCard.setCardBackgroundColor(ResourcesCompat.getColor(resources,R.color.translucentGrey,null))
                    phoneScanCard.alpha = 1F
                    cardState = 0
                    closeKeyboard()
                    //context?.let { hideKeyboardFrom(it,view) }
                    phoneScanImage.visibility = View.VISIBLE
                    phoneScanText.visibility = (View.VISIBLE)
                    phoneScanActiveField.visibility = View.INVISIBLE
                    phoneScanActiveText.visibility = (View.GONE)
                    scanTagImage.visibility = View.VISIBLE
                    scanTagText.visibility = (View.VISIBLE)
                    phoneScanText.text = "Host on this phone"
                    scanTagText.text = "Host with scan tag"
                    scanTagActiveField.visibility = View.INVISIBLE
                    scanTagActiveText.visibility = (View.GONE)
                    phoneScanExit.visibility = View.GONE
                    scanTagExit.visibility = View.GONE
                    phoneScanActiveField.text = scanTagActiveField.text
                }

            }



            //Toast.makeText(context,cardState.toString(),Toast.LENGTH_SHORT).show()
            (activity as MainActivity).notificationsCardState = cardState

        }



    }

    fun closeKeyboard() {
        val activity = activity

        val view = activity?.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
        }
    }

    fun hideKeyboardFrom(
        context: Context,
        view: View
    ) {
        val imm =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
