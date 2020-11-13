package com.raywenderlich.android.colorchat.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Channel {
    @DocumentId
    var id: String? = null
    var name: String? = null
    var description: String? = null

    constructor()

    constructor(id: String?, name: String?, description: String?){
        this.id = id
        this.name = name
        this.description = description
    }
}