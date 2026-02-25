package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamDetail (
    @SerialName("teamId")
    var Id: Int,

    @SerialName("teamName")
    var Name: String,

    @SerialName("teamEstablished")
    var Established: String,
)