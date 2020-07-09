package com.example.githubsearchusers.ui.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class UsersLoadStateAdapter(private val retry: () -> Unit)
    : LoadStateAdapter<NetworkStateItemViewHolder>() {

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState)
            : NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder(parent, retry)
    }
}