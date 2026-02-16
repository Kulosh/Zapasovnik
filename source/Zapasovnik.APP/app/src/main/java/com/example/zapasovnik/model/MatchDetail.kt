package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchDetail (
    @SerialName("matchId")
    var Id: Int,

    @SerialName("team1")
    var Team1: String,

    @SerialName("team2")
    var Team2: String,

    @SerialName("date")
    var Date: String,

    @SerialName("league")
    var League: String
)