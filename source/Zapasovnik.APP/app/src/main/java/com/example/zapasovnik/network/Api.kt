package com.example.zapasovnik.network

import com.example.zapasovnik.model.FavPlayer
import com.example.zapasovnik.model.JwtResponse
import com.example.zapasovnik.model.League
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.model.MatchDetail
import com.example.zapasovnik.model.PlayerDetail
import com.example.zapasovnik.model.Team
import com.example.zapasovnik.model.TeamDetail
import kotlinx.serialization.json.JsonObject
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {
    @GET("TeamMatches")
    suspend fun getTeamMatches(): List<Match>

    @GET("Players")
    suspend fun getPlayers(): List<FavPlayer>

    @GET("Teams")
    suspend fun getTeams(): List<Team>

    @GET("Leagues")
    suspend fun getLeagues(
        @Header("Authorization") token: String
    ): List<League>

    @GET("LeagueDetail/{id}")
    suspend fun getLeagueDetail(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String
    ): Response<League>

    @POST("PlayerDetail")
    suspend fun postPlayerDetail(
        @Body user: JsonObject
    ): Response<PlayerDetail>

    @POST("MatchDetail")
    suspend fun postMatchDetail(
        @Body user: JsonObject
    ): Response<MatchDetail>

    @POST("TeamDetail")
    suspend fun postTeamDetail(
        @Body user: JsonObject
    ): Response<TeamDetail>

    @POST("User")
    suspend fun postUser(
        @Body user: JsonObject
    ): ResponseBody

    @POST("chgpwd")
    suspend fun postChangePassword(
        @Body changePassword: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("favPlayer")
    suspend fun postFavPlayer (
        @Body userId: JsonObject,
        @Header("Authorization") authorization: String
    ): List<FavPlayer>

    @POST("favMatch")
    suspend fun postFavMatch (
        @Body userId: JsonObject,
        @Header("Authorization") authorization: String
    ): List<Match>

    @POST("favTeams")
    suspend fun postFavTeams (
        @Body userId: JsonObject,
        @Header("Authorization") authorization: String
    ): List<Team>

    @POST("Register")
    suspend fun postRegister (
        @Body newUser: JsonObject
    ): ResponseBody

    @POST("AddPlayer")
    suspend fun postAddPlayer (
        @Body newPlayer: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("AddFavPlayer")
    suspend fun postAddFavPlayer (
        @Body favPlayer: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("DeleteFavPlayer")
    suspend fun postDeleteFavPlayer(
        @Body favPlayer: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("AddMatch")
    suspend fun postAddMatch(
        @Body newMatch: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("AddFavMatch")
    suspend fun postAddFavMatch(
        @Body favMatch: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("DeleteFavMatch")
    suspend fun postDeleteFavMatch(
        @Body favMatch: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("AddTeam")
    suspend fun postAddTeam(
        @Body newTeam: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("AddFavTeam")
    suspend fun postAddFavTeam(
        @Body favTeam: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("DeleteFavTeam")
    suspend fun postDeleteFavTeam(
        @Body favTeam: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @POST("AddLeague")
    suspend fun postAddLeague(
        @Body newLeague: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @DELETE("DeletePlayer/{id}")
    suspend fun deletePlayer(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @DELETE("DeleteMatch/{id}")
    suspend fun deleteMatch(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @DELETE("DeleteTeam/{id}")
    suspend fun deleteTeam(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @DELETE("DeleteLeague/{id}")
    suspend fun deleteLeague(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @PATCH("EditPlayer/{id}")
    suspend fun patchEditPlayer(
        @Path("id") id: Int,
        @Body player: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @PATCH("EditMatch/{id}")
    suspend fun patchEditMatch(
        @Path("id") id: Int,
        @Body match: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @PATCH("EditTeam/{id}")
    suspend fun patchEditTeam(
        @Path("id") id: Int,
        @Body team: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>

    @PATCH("EditLeague/{id}")
    suspend fun patchEditLeague(
        @Path("id") id: Int,
        @Body league: JsonObject,
        @Header("Authorization") authorization: String
    ): Response<Boolean>
}