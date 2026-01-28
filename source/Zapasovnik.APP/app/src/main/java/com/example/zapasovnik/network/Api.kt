package com.example.zapasovnik.network

import com.example.zapasovnik.model.Match
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("TeamMatches")
    suspend fun getTeamMatches(): List<Match>

    @POST("Login")
    suspend fun postLogin(
        @Body loginString: JsonObject
    ): Response<Boolean>
}