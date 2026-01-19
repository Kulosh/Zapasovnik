package com.example.zapasovnik.network

import com.example.zapasovnik.model.Match
import retrofit2.http.GET

interface Api {
    @GET("matches")
    suspend fun getMatches(): List<Match>
}