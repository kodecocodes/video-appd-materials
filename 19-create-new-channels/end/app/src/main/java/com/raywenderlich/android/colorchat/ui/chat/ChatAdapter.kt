package com.raywenderlich.android.colorchat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.raywenderlich.android.colorchat.R
import com.raywenderlich.android.colorchat.data.model.Message
import kotlinx.android.synthetic.main.message_item.view.*
import java.text.SimpleDateFormat
import java.util.*

open class ChatAdapter(query: Query)
    : FirestoreAdapter<ChatAdapter.MessageViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject(Message::class.java))
    }

    inner class MessageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(message: Message?) = with(view) {
            message?.let {

                val pattern = context.getString(R.string.date_format)
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                val date: String = simpleDateFormat.format(it.timestamp)

                messageTextView.text = it.text
                timestampTextView.text = date
                senderTextView.text = it.userName
            }
        }
    }
}