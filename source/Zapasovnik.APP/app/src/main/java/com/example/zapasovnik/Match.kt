package com.example.zapasovnik

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Match (
    @SerialName(value = "team1")
    var Team1: String,

    @SerialName(value = "date")
    var Date: String,

    @SerialName("team2")
    var Team2: String
)