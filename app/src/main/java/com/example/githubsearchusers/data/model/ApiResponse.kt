package com.example.githubsearchusers.data.model

import com.example.githubsearchusers.data.model.user.User
import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "total_count") val totalCount: Int?,
    @Json(name = "incomplete_result") val incompleteResult: Boolean?,
    @Json(name = "items") val items: List<User>
)