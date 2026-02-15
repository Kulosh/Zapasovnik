package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Match (
    @SerialName(value = "matchId")
    var Id: Int,

    @SerialName(value = "matchDate")
    var Date: String,

    @SerialName(value = "team1")
    var Team1: String,

    @SerialName("team2")
    var Team2: String
)