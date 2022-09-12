package com.example.mysubmission1

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = resources.getString(R.string.Detail_view)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        val tvAvatar: ImageView = findViewById(R.id.im_detail_avatar)
        val tvName: TextView = findViewById(R.id.tv_detail_name)
        val tvUsername: TextView = findViewById(R.id.tv_detail_username)
        val tvFollowerNumber: TextView = findViewById(R.id.tv_detail_follower)
        val tvFollowingNumber: TextView = findViewById(R.id.tv_detail_following)
        val tvRepoNumber: TextView = findViewById(R.id.tv_detail_repo)
        val tvCompanyValue: TextView = findViewById(R.id.tv_detail_company)
        val tvLocationValue: TextView = findViewById(R.id.tv_detail_location)

        val user = intent.getParcelableExtra<User>(USER) as User

        tvAvatar.setImageResource(user.avatar)
        tvName.text = user.name
        tvUsername.text = user.username
        tvFollowerNumber.text = resources.getString(R.string.follower, user.followers)
        tvFollowingNumber.text = resources.getString(R.string.following, user.followings)
        tvRepoNumber.text = resources.getString(R.string.repo, user.repository)
        tvCompanyValue.text = resources.getString(R.string.company, user.company)
        tvLocationValue.text = resources.getString(R.string.location, user.location)


    }

    companion object {
        const val USER = "key_user"
    }
}