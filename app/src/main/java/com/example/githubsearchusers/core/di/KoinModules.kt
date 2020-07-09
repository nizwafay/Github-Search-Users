package com.example.githubsearchusers.core.di

import com.example.githubsearchusers.core.createRetrofitClient
import com.example.githubsearchusers.data.api.SearchUserApi
import com.example.githubsearchusers.data.repository.UsersRepository
import com.example.githubsearchusers.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(listOf(
        apiModule,
        repositoryModule,
        viewModelModule
    ))
}

val apiModule: Module = module {
    factory { createRetrofitClient(context = androidContext()).create(SearchUserApi::class.java) }
}

val repositoryModule: Module = module {
    factory { UsersRepository(api = get()) }
}

val viewModelModule: Module = module {
    viewModel { MainViewModel(repository = get()) }
}