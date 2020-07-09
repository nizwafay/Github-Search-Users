package com.example.githubsearchusers.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearchusers.R
import com.example.githubsearchusers.databinding.NetworkStateItemBinding
import com.example.githubsearchusers.util.getErrorMessage


/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemViewHolder(
    parent: ViewGroup,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.network_state_item, parent, false)
) {
    private val binding = NetworkStateItemBinding.bind(itemView)
    private val progressBar = binding.progressBar
    private val errorMsg = binding.errorText
    private val retry = binding.retryButton
        .also {
            it.setOnClickListener {
                retryCallback()
            }
        }

    fun bindTo(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.getErrorMessage().isNullOrBlank()
        errorMsg.text = (loadState as? LoadState.Error)?.error?.getErrorMessage()
    }
}