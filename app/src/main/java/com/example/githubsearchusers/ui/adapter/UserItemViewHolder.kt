package com.example.githubsearchusers.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearchusers.GlideRequests
import com.example.githubsearchusers.R
import com.example.githubsearchusers.data.model.user.User
import com.example.githubsearchusers.databinding.UserItemBinding

class UserItemViewHolder(
    parent: ViewGroup,
    private val glide: GlideRequests
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
) {
    private val binding = UserItemBinding.bind(itemView)
    private val username = binding.username
    private val avatar = binding.avatar

    fun bindTo(user : User?) {
        user?.let {
            username.text = it.login
            glide.load(it.avatarUrl)
                .placeholder(ColorDrawable(Color.GRAY))
                .into(avatar)
        }
    }
}