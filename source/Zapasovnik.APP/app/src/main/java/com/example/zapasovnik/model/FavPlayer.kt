package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavPlayer (
    @SerialName("id")
    var Id: Int,

    @SerialName("fName")
    var FName: String,

    @SerialName("lName")
    var LName: String,

    @SerialName("team")
    var Team: String? = "No team",
)