package com.example.githubappsv3.View

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
import com.example.githubappsv3.Favorite.Factory.FavoriteViewModelFactory
import com.example.githubappsv3.Model.Model
import com.example.githubappsv3.R
import com.example.githubappsv3.View.Adapter.FollowTabAdapter
import com.example.githubappsv3.ViewModel.DetailsViewModel
import com.example.githubappsv3.databinding.ActivityDetailsBinding
import com.example.githubappsv3.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = resources.getString(R.string.details_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
        val user = intent.getStringExtra(USERNAME_EXTRA)
        binding.viewPager.adapter = FollowTabAdapter(this, user)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.followers_title)
                1 -> tab.text = resources.getString(R.string.following_title)
            }
        }.attach()
        prepareViewModel(this@DetailsActivity)
        if (user != null) {
            showLoading(true)
            viewModel.fetchUser(user)
        }
        binding.btFavorite.setOnClickListener { viewModel.addToFavorite() }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu!!.findItem(R.id.action_favorite).isVisible = false
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

    private fun prepareViewModel(detailsActivity: DetailsActivity) {
        val factory= FavoriteViewModelFactory.getInstance(detailsActivity.application)
        viewModel = ViewModelProvider(detailsActivity,factory).get(DetailsViewModel::class.java)
        viewModel.getUser().observe(this, Observer { userData ->
            showLoading(false)
            if (userData != null) {
                showDetails(userData)
                showError(userData.t)
                viewModel.checkFavorite()
            }
        })

        viewModel.isFavorite().observe(this, Observer {
            if (it) binding.btFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            else binding.btFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        })

    }

    private fun showDetails(user: Model.UserData) {
        Glide.with(this)
            .load(user.avatar)
            .into(binding.ivPhoto)

        binding.tvName.text = user.name
        binding.tvUsername.text = user.login

        if (user.location.isNullOrEmpty()) binding.tvLocation.visibility = View.GONE
        else binding.tvLocation.text = user.location

        if (user.company.isNullOrEmpty()) binding.tvCompany.visibility = View.GONE
        else binding.tvCompany.text = user.company

        binding.tvRepositories.text = resources.getString(R.string.repositories_count, user.repos)
        binding.tvFollowers.text = resources.getString(R.string.followers_count, user.followers)
        binding.tvFollowing.text = resources.getString(R.string.following_count, user.following)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.pbLoading.visibility = View.VISIBLE
            binding.clContent.visibility = View.INVISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
            binding.clContent.visibility = View.VISIBLE
        }
    }

    private fun showError(t: Throwable?) {
        if (t != null) {
            Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
            t.printStackTrace()
        }
    }

    companion object{
        const val USERNAME_EXTRA = "USERNAME_EXTRA"
    }
}