package com.raywenderlich.android.colorchat.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.raywenderlich.android.colorchat.R

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }

    fun sendClicked(view: View){}

    companion object {
        private const val LIMIT = 50
        const val KEY_CHANNEL_ID = "key_restaurant_id"
        const val KEY_CHANNEL_NAME = "key_channel_name"
        private const val MESSAGE_COLLECTION = "messages"
        private const val CHANNEL_COLLECTION = "channels"
        private const val TIMESTAMP = "timestamp"
    }
}