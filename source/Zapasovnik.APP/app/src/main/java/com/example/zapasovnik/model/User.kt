package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("userId")
    var userId: Int,

    @SerialName("username")
    val username: String,

    @SerialName("email")
    val email: String,

    @SerialName("success")
    val success: Boolean
)
