package com.example.githubappsv3.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubappsv3.Favorite.Factory.FavoriteViewModelFactory
import com.example.githubappsv3.Favorite.FavoriteEntity
import com.example.githubappsv3.Model.Model
import com.example.githubappsv3.R
import com.example.githubappsv3.View.Adapter.FavoriteAdapter
import com.example.githubappsv3.View.Adapter.UserAdapter
import com.example.githubappsv3.ViewModel.FavoriteViewModel
import com.example.githubappsv3.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var viewModel:FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = resources.getString(R.string.menu_title_favorite)
        prepareRecyclerView()
        prepareViewModel(this@FavoriteActivity)

    }

    private fun prepareRecyclerView() {
        favoriteAdapter = FavoriteAdapter()
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity, RecyclerView.VERTICAL, false)
            adapter = favoriteAdapter.apply {
                onItemClickCallback = object :
                    FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: FavoriteEntity) {
                        val intent = Intent(this@FavoriteActivity, DetailsActivity::class.java)
                        intent.putExtra(DetailsActivity.USERNAME_EXTRA, data.username)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun prepareViewModel(favoriteActivity: FavoriteActivity) {

        val factory=FavoriteViewModelFactory.getInstance(favoriteActivity.application)
        viewModel = ViewModelProvider(favoriteActivity,factory).get(FavoriteViewModel::class.java)

        viewModel.getAllDataFavorites().observe(this, Observer { favoriteList ->
            showLoading(false)
            if (favoriteList != null) {
                if (favoriteList.isNotEmpty()) {
                    favoriteAdapter.setData(ArrayList(favoriteList))
                } else {
                    showNotFound()
                }
            }
        })
    }

    private fun showNotFound() {
        binding.tvNotfound.visibility = View.VISIBLE
        binding.rvFavorite.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.tvNotfound.visibility = View.GONE
            binding.rvFavorite.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE
        }
        else {
            binding.tvNotfound.visibility = View.GONE
            binding.rvFavorite.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.GONE
        }
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

}