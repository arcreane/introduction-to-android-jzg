package com.example.shaketorefreshjokes
import retrofit2.Call
import retrofit2.http.GET

interface JokeApiService {
    @GET("random_joke")
    fun getRandomJoke(): Call<JokeResponse>
}