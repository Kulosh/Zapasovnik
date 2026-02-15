package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class League (
    @SerialName("leagueId")
    var LeagueId: Int,

    @SerialName("leagueName")
    var LeagueName: String
)