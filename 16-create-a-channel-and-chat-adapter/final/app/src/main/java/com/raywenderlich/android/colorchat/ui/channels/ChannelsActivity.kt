package com.raywenderlich.android.colorchat.ui.channels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.raywenderlich.android.colorchat.R

class ChannelsActivity : AppCompatActivity() {

    private lateinit var channelDialog: ChannelDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)
        channelDialog = ChannelDialogFragment()
    }

    fun showChannelDialog(view: View){
        channelDialog.show(supportFragmentManager, ChannelDialogFragment.TAG)
    }
}