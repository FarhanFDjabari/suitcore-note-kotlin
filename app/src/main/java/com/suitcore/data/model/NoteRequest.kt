package com.suitcore.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList

open class NoteRequest(title: String, body: String, tags: List<String>, updatedAt: String?) {
    @SerializedName("title")
    var title: String? = title

    @SerializedName("body")
    var body: String? = body

    @SerializedName("tags")
    var tags: List<String>? = tags

    @SerializedName("updatedAt")
    var updatedAt: String? = updatedAt
}