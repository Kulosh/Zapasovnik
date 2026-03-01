package com.example.zapasovnik.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JwtResponse(
    @SerialName("token")
    val token: String,

    @SerialName("user")
    val user: User,
)
