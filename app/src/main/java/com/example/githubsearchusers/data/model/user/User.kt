package com.example.githubsearchusers.data.model.user

import com.squareup.moshi.Json

data class User(
    @Json(name = "id") val id: Int?,
    @Json(name = "login") val login: String?,
    @Json(name = "avatar_url") val avatarUrl: String?
)