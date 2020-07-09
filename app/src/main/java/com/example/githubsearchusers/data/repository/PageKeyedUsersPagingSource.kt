package com.example.githubsearchusers.data.repository

import androidx.paging.PagingSource
import com.example.githubsearchusers.data.model.user.User
import retrofit2.HttpException
import java.io.IOException

class PageKeyedUsersPagingSource(
    private val repository: UsersRepository,
    private val userQuery: String
) : PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val pageNumber = params.key ?: 1

            val response = repository.getListUsers(userQuery, pageNumber)

            val nextKey = if (response.items.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = response.items,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}