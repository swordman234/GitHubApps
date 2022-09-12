package com.example.githubappsv3.View.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubappsv3.View.DetailsActivity
import com.example.githubappsv3.View.FollowersFragment
import com.example.githubappsv3.View.FollowingFragment

class FollowTabAdapter(fragment: FragmentActivity, private val user: String?) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment =
                FollowersFragment()
            1 -> fragment =
                FollowingFragment()
        }
        val bundle = Bundle()
        bundle.putString(DetailsActivity.USERNAME_EXTRA, user)
        fragment?.arguments = bundle
        return fragment as Fragment
    }
}