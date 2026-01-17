package com.example.zapasovnik.network

import com.example.zapasovnik.Match
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://api.kulosh.eu/zapasovnik"
private val retrofit = Retrofit
    .Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()
object ZapasovnikApi {
    val retrofitService: Api by lazy {
        retrofit.create(Api::class.java)
    }
}

interface Api {
    @GET("matches")
    suspend fun getMatches(): List<Match>
}