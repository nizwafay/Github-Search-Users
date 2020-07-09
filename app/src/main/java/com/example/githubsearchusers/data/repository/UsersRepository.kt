package com.example.githubsearchusers.data.repository

import com.example.githubsearchusers.data.api.SearchUserApi
import com.example.githubsearchusers.data.model.ApiResponse

class UsersRepository(private val api: SearchUserApi) {
    suspend fun getListUsers(
        user: String,
        page: Int
    ): ApiResponse =
        api.getUsers(user, page)
}