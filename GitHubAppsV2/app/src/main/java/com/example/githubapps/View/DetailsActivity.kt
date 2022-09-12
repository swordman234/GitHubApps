package com.example.githubapps.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubapps.Model.Model
import com.example.githubapps.R
import com.example.githubapps.View.Adapter.FollowTabAdapter
import com.example.githubapps.ViewModel.DetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val USERNAME_EXTRA = "USERNAME_EXTRA"
    }

    private lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportActionBar?.title = resources.getString(R.string.details_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
        val user = intent.getStringExtra(USERNAME_EXTRA)
        viewPager.adapter = FollowTabAdapter(this, user)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.followers_title)
                1 -> tab.text = resources.getString(R.string.following_title)
            }
        }.attach()
        prepareViewModel()
        if (user != null) {
            showLoading(true)
            viewModel.fetchUser(user)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val mIntent = Intent(this, SettingsActivity::class.java)
                startActivity(mIntent)
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun prepareViewModel() {
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        viewModel.getUser().observe(this, Observer { userData ->
            showLoading(false)
            if (userData != null) {
                showDetails(userData)
                showError(userData.t)
            }
        })
    }

    private fun showDetails(user: Model.UserData) {
        Glide.with(this)
            .load(user.avatar)
            .into(iv_photo)

        tv_name.text = user.name
        tv_username.text = user.login

        if (user.location.isNullOrEmpty()) tv_location.visibility = View.GONE
        else tv_location.text = user.location

        if (user.company.isNullOrEmpty()) tv_company.visibility = View.GONE
        else tv_company.text = user.company

        tv_repositories.text = resources.getQuantityString(R.plurals.repositories_count, user.repos, user.repos, user.repos)
        tv_followers.text = resources.getQuantityString(R.plurals.followers_count, user.followers, user.followers, user.followers)
        tv_following.text = resources.getQuantityString(R.plurals.following_count, user.following, user.following, user.following)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            pb_loading.visibility = View.VISIBLE
            cl_content.visibility = View.INVISIBLE
        } else {
            pb_loading.visibility = View.GONE
            cl_content.visibility = View.VISIBLE
        }
    }

    private fun showError(t: Throwable?) {
        if (t != null) {
            Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
            t.printStackTrace()
        }
    }
}