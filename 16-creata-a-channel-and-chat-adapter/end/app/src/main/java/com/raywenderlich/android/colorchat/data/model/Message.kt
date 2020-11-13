package com.raywenderlich.android.colorchat.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@IgnoreExtraProperties
class Message {
    var text: String? = null
    var userId: String? = null
    var userName: String? = null

    @ServerTimestamp
    var timestamp: Date? = null

    constructor()

    constructor(text: String?,
                userId: String,
                userName: String?,
                timestamp: Date?) {
        this.userName = userName
        this.text = text
        this.userId = userId
        this.timestamp = timestamp
    }


}