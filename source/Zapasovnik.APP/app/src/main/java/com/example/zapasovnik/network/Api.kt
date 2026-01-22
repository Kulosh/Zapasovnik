package com.example.zapasovnik.network

import com.example.zapasovnik.model.Match
import retrofit2.http.GET

interface Api {
    @GET("TeamMatches")
    suspend fun getTeamMatches(): List<Match>
}