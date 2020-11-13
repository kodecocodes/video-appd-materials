package com.raywenderlich.android.colorchat.ui.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.raywenderlich.android.colorchat.R
import com.raywenderlich.android.colorchat.data.model.Channel
import kotlinx.android.synthetic.main.dialog_channel.*

class ChannelDialogFragment : DialogFragment(), View.OnClickListener {

    internal interface ChannelListener {
        fun onChannel(channel: Channel)
    }

    private var channelListener: ChannelListener? = null

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ChannelListener) {
            channelListener = context
        }
    }

    private fun onSubmitClicked(view: View?){
        val channel = Channel(
            null,
            channel_name_text.text.toString(),
            channel_description_text.text.toString()
        )
        channel_name_text.text.clear()
        channel_description_text.text.clear()
        channelListener?.onChannel(channel)
        dismiss()
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