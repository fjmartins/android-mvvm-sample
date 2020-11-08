package com.fjmartins.forexrates.model

import com.google.gson.annotations.SerializedName

data class CurrencyListResponse (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("terms")
    val terms: String,
    @SerializedName("privacy")
    val privacy: String,
    @SerializedName("currencies")
    val currencies: Map<String, String>,
    @SerializedName("timestamp")
    val timestamp: Int
)