package com.example.zapasovnik.network

import com.example.zapasovnik.model.FavPlayer
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.model.PlayerDetail
import com.example.zapasovnik.model.Team
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {
    @GET("TeamMatches")
    suspend fun getTeamMatches(): List<Match>

    @GET("Players")
    suspend fun getPlayers(): List<FavPlayer>

    @GET("Teams")
    suspend fun getTeams(): List<Team>

    @GET("PlayerDetail/{id}")
    suspend fun getPlayerDetail(@Path("id") id: Int): Response<PlayerDetail>

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

    @POST("AddPlayer")
    suspend fun postAddPlayer (
        @Body newPlayer: JsonObject
    ): Response<Boolean>

    @POST("AddFavPlayer")
    suspend fun postAddFavPlayer (
        @Body favPlayer: JsonObject
    ): Response<Boolean>

    @POST("DeleteFavPlayer")
    suspend fun postDeleteFavPlayer(
        @Body favPlayer: JsonObject
    ): Response<Boolean>

    @DELETE("DeletePlayer/{id}")
    suspend fun deletePlayer(@Path("id") id: Int): Response<Boolean>
}