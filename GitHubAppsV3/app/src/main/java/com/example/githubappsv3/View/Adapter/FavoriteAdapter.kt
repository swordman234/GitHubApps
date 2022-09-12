package com.example.githubappsv3.View.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubappsv3.Favorite.FavoriteEntity
import com.example.githubappsv3.Model.Model
import com.example.githubappsv3.databinding.ListUserBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>(){

    lateinit var onItemClickCallback: OnItemClickCallback
    private var users = ArrayList<FavoriteEntity>()


    fun setData(users: List<FavoriteEntity>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(itemView: ListUserBinding): RecyclerView.ViewHolder(itemView.root) {
        val avatar = itemView.ivPhoto
        val name = itemView.tvName
        val id = itemView.tvId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val user = users[position]

        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .into(holder.avatar)
        holder.name.text = user.username
        holder.id.text = user.id.toString()
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(user)
        }
    }

    override fun getItemCount(): Int = users.size

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteEntity)
    }
}