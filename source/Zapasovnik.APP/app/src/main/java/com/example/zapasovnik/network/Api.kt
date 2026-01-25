package com.example.zapasovnik.network

import com.example.zapasovnik.model.Match
import com.example.zapasovnik.model.User
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("TeamMatches")
    suspend fun getTeamMatches(): List<Match>

    @POST("Users")
    suspend fun postUser(): User
}