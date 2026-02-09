package com.example.zapasovnik.network

import com.example.zapasovnik.model.FavPlayer
import com.example.zapasovnik.model.Match
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("TeamMatches")
    suspend fun getTeamMatches(): List<Match>

    @POST("User")
    suspend fun postUser(
        @Body user: JsonObject
    ): Response<JsonObject>

//    @POST("Login")
//    suspend fun postLogin(
//        @Body loginString: JsonObject
//    ): Response<Boolean>

    @POST("chgpwd")
    suspend fun postChangePassword(
        @Body changePassword: JsonObject
    ): Response<Boolean>

    @POST("favPlayer")
    suspend fun postFavPlayer (
        @Body userId: JsonObject
    ): List<FavPlayer>

    @POST("Register")
    suspend fun postRegister (
        @Body newUser: JsonObject
    ): Response<JsonObject>
}