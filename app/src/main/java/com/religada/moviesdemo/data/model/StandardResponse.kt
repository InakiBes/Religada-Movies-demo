package com.religada.moviesdemo.data.model

import com.google.gson.annotations.SerializedName

data class StandardResponse(
    @SerializedName("error_code") val errorCode: Int = -1,
    @SerializedName("error_msg") val errorMsg: String = "",
)

