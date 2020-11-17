package com.raywenderlich.android.colorchat.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.raywenderlich.android.colorchat.R
import kotlinx.android.synthetic.main.dialog_channel.*

class ChannelDialogFragment : DialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_channel, container, false)
        v.findViewById<View>(R.id.submit_channel_button).setOnClickListener(this)
        v.findViewById<View>(R.id.cancel_channel_button).setOnClickListener(this)
        return v
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun onCancelClicked(view: View?){
        channel_name_text.text.clear()
        channel_description_text.text.clear()
        dismiss()
    }

    private fun onSubmitClicked(view: View?){
        //TODO Implement method
    }

    companion object {
        const val TAG = "ChannelDialog"
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.submit_channel_button -> onSubmitClicked(v)
            R.id.cancel_channel_button -> onCancelClicked(v)
        }
    }
}