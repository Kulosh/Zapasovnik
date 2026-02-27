package com.example.zapasovnik.network

import com.example.zapasovnik.model.FavPlayer
import com.example.zapasovnik.model.League
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.model.MatchDetail
import com.example.zapasovnik.model.PlayerDetail
import com.example.zapasovnik.model.Team
import com.example.zapasovnik.model.TeamDetail
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

    @GET("Leagues")
    suspend fun getLeagues(): List<League>

    @GET("LeagueDetail/{id}")
    suspend fun getLeagueDetail(@Path("id") id: Int): Response<League>

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
    ): Response<JsonObject>

    @POST("chgpwd")
    suspend fun postChangePassword(
        @Body changePassword: JsonObject
    ): Response<Boolean>

    @POST("favPlayer")
    suspend fun postFavPlayer (
        @Body userId: JsonObject
    ): List<FavPlayer>

    @POST("favMatch")
    suspend fun postFavMatch (
        @Body userId: JsonObject
    ): List<Match>

    @POST("favTeams")
    suspend fun postFavTeams (
        @Body userId: JsonObject
    ): List<Team>

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

    @POST("AddMatch")
    suspend fun postAddMatch(
        @Body newMatch: JsonObject
    ): Response<Boolean>

    @POST("AddFavMatch")
    suspend fun postAddFavMatch(
        @Body favMatch: JsonObject
    ): Response<Boolean>

    @POST("DeleteFavMatch")
    suspend fun postDeleteFavMatch(
        @Body favMatch: JsonObject
    ): Response<Boolean>

    @POST("AddTeam")
    suspend fun postAddTeam(
        @Body newTeam: JsonObject
    ): Response<Boolean>

    @POST("AddFavTeam")
    suspend fun postAddFavTeam(
        @Body favTeam: JsonObject
    ): Response<Boolean>

    @POST("DeleteFavTeam")
    suspend fun postDeleteFavTeam(
        @Body favTeam: JsonObject
    ): Response<Boolean>

    @POST("AddLeague")
    suspend fun postAddLeague(
        @Body newLeague: JsonObject
    ): Response<Boolean>

    @DELETE("DeletePlayer/{id}")
    suspend fun deletePlayer(@Path("id") id: Int): Response<Boolean>

    @DELETE("DeleteMatch/{id}")
    suspend fun deleteMatch(@Path("id") id: Int): Response<Boolean>

    @DELETE("DeleteTeam/{id}")
    suspend fun deleteTeam(@Path("id") id: Int): Response<Boolean>

    @DELETE("DeleteLeague/{id}")
    suspend fun deleteLeague(@Path("id") id: Int): Response<Boolean>
}