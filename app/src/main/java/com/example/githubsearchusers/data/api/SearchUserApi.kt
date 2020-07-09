package com.example.githubsearchusers.data.api

import com.example.githubsearchusers.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchUserApi {
    @GET("search/users")
    suspend fun getUsers(@Query("q") user: String,
                         @Query("page") page: Int): ApiResponse
}