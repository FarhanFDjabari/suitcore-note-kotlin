package com.suitcore.data.remote.wrapper

import com.google.gson.annotations.SerializedName

class Result<T> {
    @SerializedName("result")
    var data: T? = null
}