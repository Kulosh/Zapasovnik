package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User (
    @SerialName("userName")
    val username: String,
    @SerialName("userPassword")
    val userPassword: String,
)