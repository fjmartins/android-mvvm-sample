package com.fjmartins.forexrates.model

import com.google.gson.annotations.SerializedName

data class LiveQuotesResponse(
    @SerializedName("privacy")
    val privacy: String,
    @SerializedName("quotes")
    val quotes: Quotes,
    @SerializedName("source")
    val source: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("terms")
    val terms: String,
    @SerializedName("timestamp")
    val timestamp: Int
)