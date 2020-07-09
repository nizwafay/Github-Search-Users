package com.example.githubsearchusers.ui

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubsearchusers.data.model.user.User
import com.example.githubsearchusers.data.repository.PageKeyedUsersPagingSource
import com.example.githubsearchusers.data.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class MainViewModel(private val repository: UsersRepository): ViewModel() {

    private var _userKeyword = MutableLiveData<String>()
    val userKeyword: LiveData<String>
        get() = _userKeyword

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    private fun createFlow(user: String): Flow<PagingData<User>> {
        return Pager(
            // Configure how data is loaded by passing additional properties to
            // PagingConfig, such as prefetchDistance.
            PagingConfig(
                initialLoadSize = 100,
                pageSize = 100,
                prefetchDistance = 25
            )
        ) { PageKeyedUsersPagingSource(repository, user) }
            .flow
            .cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val users = flowOf(
        clearListCh.consumeAsFlow().map { PagingData.empty<User>() },
        userKeyword
            .asFlow()
            .flatMapLatest { createFlow(it) }
    ).flattenMerge(2)

    fun searchUser(user: String) {
        if (user != userKeyword.value) {
            clearListCh.offer(Unit)
            _userKeyword.value = user
        }
    }
}