package com.example.githubapps.View.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapps.Model.Model
import com.example.githubapps.R
import kotlinx.android.synthetic.main.list_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    lateinit var onItemClickCallback: OnItemClickCallback
    private val users = ArrayList<Model.UserData>()

    fun setData(users: ArrayList<Model.UserData>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: Model.UserData) {
            with (itemView) {
                Glide.with(context)
                    .load(user.avatar)
                    .into(iv_photo)
                tv_name.text = user.login
                tv_id.text = user.id.toString()
                setOnClickListener { onItemClickCallback.onItemClicked(user) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Model.UserData)
    }
}