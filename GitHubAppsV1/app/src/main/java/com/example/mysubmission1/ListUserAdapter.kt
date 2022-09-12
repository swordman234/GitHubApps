package com.example.mysubmission1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListUserAdapter(private val listUser:ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.imavatar.setImageResource(user.avatar)
        holder.tvname.text = user.name
        holder.tvcompany.text = user.company
        holder.tvfollowerNumber.text = user.followers
        holder.tvfollowingNumber.text = user.followings
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imavatar: ImageView = itemView.findViewById(R.id.img_photo)
        var tvname:TextView = itemView.findViewById(R.id.tv_name)
        var tvcompany:TextView = itemView.findViewById(R.id.tv_company)
        var tvfollowerNumber:TextView = itemView.findViewById(R.id.tv_follower)
        var tvfollowingNumber:TextView = itemView.findViewById(R.id.tv_following)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}