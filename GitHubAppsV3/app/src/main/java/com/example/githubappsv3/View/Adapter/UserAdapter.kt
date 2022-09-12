package com.example.githubappsv3.View.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubappsv3.Favorite.FavoriteEntity
import com.example.githubappsv3.Model.Model
import com.example.githubappsv3.databinding.ListUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    lateinit var onItemClickCallback: OnItemClickCallback
    private var users = ArrayList<Model.UserData>()


    fun setData(users: ArrayList<Model.UserData>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .into(holder.avatar)
        holder.name.text = user.login
        holder.id.text = user.id.toString()
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }

    inner class UserViewHolder(itemView: ListUserBinding): RecyclerView.ViewHolder(itemView.root) {
        val avatar = itemView.ivPhoto
        val name = itemView.tvName
        val id = itemView.tvId

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Model.UserData)
    }
}