package com.suitcore.data.model.deserelializer

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.suitcore.data.model.Note
import java.lang.reflect.Type

class NoteDeserializer: SuitCoreJsonDeserializer<Note>() {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Note {
        val jsonObject = json?.asJsonObject

        val note = Note()
        if (jsonObject?.has("id")!!) {
            note.id = getStringOrNullFromJson(jsonObject.get("id"))
        }

        if (jsonObject.has("title")) {
            note.title = getStringOrNullFromJson(jsonObject.get("title"))
        }

        if (jsonObject.has("body")) {
            note.body = getStringOrNullFromJson(jsonObject.get("body"))
        }

        if (jsonObject.has("tags")) {
            val gson = Gson()
            val tagType = object : TypeToken<List<String>>() {}.type
            note.tags = gson.fromJson(jsonObject.get("tags"), tagType)
        }

        return note
    }
}