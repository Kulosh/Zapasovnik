package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Team (
    @SerialName("teamId")
    var TeamId: Int,

    @SerialName("teamName")
    var TeamName: String,

    @SerialName("teamEstablished")
    var TeamEstablished: String
)