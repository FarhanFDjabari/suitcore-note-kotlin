package com.suitcore.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Note() : RealmObject() {

    @PrimaryKey
    @SerializedName("id")
    var id: String? = "id"

    @SerializedName("title")
    var title: String? = ""

    @SerializedName("body")
    var body: String? = ""

    @SerializedName("tags")
    var tags: RealmList<String>? = RealmList<String>()

    @SerializedName("createdAt")
    var createdAt: Date? = null

    @SerializedName("updatedAt")
    var updatedAt: Date? = null

}