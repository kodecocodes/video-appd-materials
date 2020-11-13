package com.raywenderlich.android.colorchat.ui.channels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.raywenderlich.android.colorchat.R
import com.raywenderlich.android.colorchat.data.model.Channel
import com.raywenderlich.android.colorchat.ui.chat.ChatActivity
import com.raywenderlich.android.colorchat.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_channels.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar

class ChannelsActivity : AppCompatActivity(),
    AnkoLogger,
    ChannelAdapter.OnChannelSelectedListener {

    private lateinit var channelDialog: ChannelDialogFragment
    private lateinit var firestore: FirebaseFirestore
    private lateinit var query: Query
    private lateinit var adapter: ChannelAdapter
    private lateinit var channelReference: DocumentReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)
        channelDialog = ChannelDialogFragment()
        auth = FirebaseAuth.getInstance()
        initFireStore()
        initRecyclerView()
    }

    fun showChannelDialog(view: View){
        channelDialog.show(supportFragmentManager, ChannelDialogFragment.TAG)
    }

    private fun initFireStore(){
        firestore = FirebaseFirestore.getInstance()
        channelReference = firestore.collection(CHANNELS).document()
        query = firestore
            .collection(CHANNELS)
            .orderBy(NAME, Query.Direction.DESCENDING)
    }

    private fun initRecyclerView(){
        adapter = object : ChannelAdapter(query, this@ChannelsActivity) {
            override fun onError(e: FirebaseFirestoreException?) {
                channelLayout.longSnackbar(getString(R.string.network_error))
                debug("FireStoreException: ${e?.message}")
            }

            override fun onDataChanged() {
                if (itemCount == 0){
                    emptyView.visibility = View.VISIBLE
                    channelsRecyclerView.visibility = View.GONE
                }else{
                    emptyView.visibility = View.GONE
                    channelsRecyclerView.visibility = View.VISIBLE
                }
                channelsRecyclerView.layoutManager = LinearLayoutManager(this@ChannelsActivity)
                channelsRecyclerView.adapter = adapter
            }
        }
    }

    override fun onChannelSelected(channel: Channel?) {
        channel?.let {
            startActivity(intentFor<ChatActivity>(ChatActivity.KEY_CHANNEL_ID to it.id,
                ChatActivity.KEY_CHANNEL_NAME to it.name))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                auth.signOut()
                startActivity(intentFor<LoginActivity>().clearTask().newTask())
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    companion object{
        private const val CHANNELS = "channels"
        private const val NAME = "name"
    }
}














