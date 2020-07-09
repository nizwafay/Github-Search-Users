package com.example.githubsearchusers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearchusers.GlideApp
import com.example.githubsearchusers.R
import com.example.githubsearchusers.core.di.injectFeature
import com.example.githubsearchusers.ui.adapter.UserAdapter
import com.example.githubsearchusers.ui.adapter.UsersLoadStateAdapter
import com.example.githubsearchusers.util.getErrorMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.network_state_item.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var pagingAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeature()
        setContentView(R.layout.activity_main)

        initSearchView()
        initAdapter()
    }

    private fun initSearchView() {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchView.clearFocus()
                    pagingAdapter.refresh()
                    viewModel.searchUser(it)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        pagingAdapter = UserAdapter(glide)
        usersRV.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = pagingAdapter.withLoadStateFooter(
                footer = UsersLoadStateAdapter { pagingAdapter.retry() }
            )
            itemAnimator = null
        }
        retryButton.setOnClickListener { pagingAdapter.retry() }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                with(loadStates.refresh) {
                    usersRV.isVisible = !(this is LoadState.Loading || this is LoadState.Error)
                    progressBar.isVisible = this is LoadState.Loading
                    retryButton.isVisible = this is LoadState.Error
                    errorText.isVisible = !(this as? LoadState.Error)?.error?.getErrorMessage().isNullOrBlank()
                    errorText.text = (this as? LoadState.Error)?.error?.getErrorMessage()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            viewModel.users.collectLatest {
                pagingAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
            pagingAdapter.dataRefreshFlow.collectLatest {
                usersRV.scrollToPosition(0)
                if (pagingAdapter.itemCount == 0) {
                    errorText.isVisible = true
                    errorText.text = getString(R.string.error_no_data, viewModel.userKeyword.value)
                }
            }
        }
    }
}