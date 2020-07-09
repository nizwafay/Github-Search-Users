package com.example.githubsearchusers.util

import com.example.githubsearchusers.App
import com.example.githubsearchusers.R
import retrofit2.HttpException
import java.io.IOException

fun Throwable.getErrorMessage(): String {
    return when {
        this is HttpException && response()?.headers()?.get("X-Ratelimit-Remaining")
            ?.toInt() == 0 -> App.context.getString(R.string.network_rate_limit_exceeded)
        this is IOException -> App.context.getString(R.string.network_no_connection)
        else -> this.message ?: "unknown error"
    }
}