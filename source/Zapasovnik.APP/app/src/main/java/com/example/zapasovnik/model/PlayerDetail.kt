package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDetail (
    @SerialName("id")
    var Id: Int,

    @SerialName("fname")
    var FName: String,

    @SerialName("lname")
    var LName: String,

    @SerialName("team")
    var Team: String? = "No team",

    @SerialName("birth")
    var Birth: String,

    @SerialName("isFavorite")
    var IsFavorite: Boolean
)