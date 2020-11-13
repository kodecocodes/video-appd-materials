package com.raywenderlich.android.colorchat.ui.channels

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.raywenderlich.android.colorchat.R
import com.raywenderlich.android.colorchat.data.model.Channel
import com.raywenderlich.android.colorchat.ui.chat.FirestoreAdapter
import kotlinx.android.synthetic.main.channel_item.view.*
import java.util.*

open class ChannelAdapter(query: Query, private val listener: OnChannelSelectedListener)
    : FirestoreAdapter<ChannelAdapter.ChannelViewHolder>(query) {

    interface OnChannelSelectedListener{
        fun onChannelSelected(channel: Channel?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.channel_item, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject(Channel::class.java), listener)
    }

    inner class ChannelViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun bind(channel: Channel?, listener: OnChannelSelectedListener) = with(view) {
            channel?.let {
                channelNameTextView.text = it.name
                descriptionTextView.text = it.description
                val rnd = Random()
                val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                channelIcon.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
            }
            this.setOnClickListener { listener.onChannelSelected(channel) }
        }
    }


}