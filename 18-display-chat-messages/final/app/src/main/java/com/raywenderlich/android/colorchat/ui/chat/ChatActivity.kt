package com.raywenderlich.android.colorchat.ui.chat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.raywenderlich.android.colorchat.R
import com.raywenderlich.android.colorchat.data.model.Message
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.design.longSnackbar

class ChatActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var messagesQuery: Query
    private lateinit var adapter: ChatAdapter
    private lateinit var channelRef: DocumentReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        auth = FirebaseAuth.getInstance()
        val channelId = intent.extras!!.getString(KEY_CHANNEL_ID)
            ?: throw IllegalArgumentException("Must pass extra $KEY_CHANNEL_ID")
        title = intent.extras!!.getString(KEY_CHANNEL_NAME)
        initFireStore(channelId)
        initRecyclerView()
    }

    private fun initFireStore(channelId: String) {
        firestore = FirebaseFirestore.getInstance()
        channelRef = firestore.collection(CHANNEL_COLLECTION).document(channelId)
        messagesQuery = channelRef
            .collection(MESSAGE_COLLECTION)
            .orderBy(TIMESTAMP, Query.Direction.ASCENDING)
    }

    private fun initRecyclerView() {
        adapter = object : ChatAdapter(messagesQuery) {

            override fun onError(e: FirebaseFirestoreException?) {
                chatLayout.longSnackbar(getString(R.string.network_error))
                debug("FireStoreException: ${e?.message}")
            }

            override fun onDataChanged() {
                if (itemCount == 0){
                    chat_empty_view.visibility = View.VISIBLE
                    messageRecyclerView.visibility = View.GONE
                }else{
                    chat_empty_view.visibility = View.GONE
                    messageRecyclerView.visibility = View.VISIBLE
                }
            }
        }
        val layoutManager = LinearLayoutManager(this@ChatActivity)
        layoutManager.stackFromEnd = true
        messageRecyclerView.layoutManager = layoutManager
        messageRecyclerView.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    public override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    private fun addMessage(text: String): Task<Void>{
        val chatReference = channelRef
            .collection(MESSAGE_COLLECTION)
            .document()
        val currentUser = auth.currentUser
        val message = Message(text, currentUser?.uid ?: "", currentUser?.displayName, null)
        return  firestore.runTransaction{transaction ->
            transaction[chatReference] = message
            null
        }
    }


    fun sendClicked(view: View){
        addMessage(messageTextField.text.toString())
            .addOnSuccessListener(this) {
                reset()
            }
            .addOnFailureListener(this) {
                chatLayout.longSnackbar(getString(R.string.network_error))
                debug("Add message failed ${it.message}")
            }
    }

    private fun reset() {
        messageTextField.setText("")
        val view = currentFocus
        if (view != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view.windowToken, 0)
        }
        messageRecyclerView.smoothScrollToPosition(messageRecyclerView.adapter!!.itemCount - 1)
        messageTextField.clearFocus()
    }

    companion object {
        private const val LIMIT = 50
        const val KEY_CHANNEL_ID = "key_restaurant_id"
        const val KEY_CHANNEL_NAME = "key_channel_name"
        private const val MESSAGE_COLLECTION = "messages"
        private const val CHANNEL_COLLECTION = "channels"
        private const val TIMESTAMP = "timestamp"
    }
}