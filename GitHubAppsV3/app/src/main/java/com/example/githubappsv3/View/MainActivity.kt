package com.example.githubappsv3.View

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubappsv3.Themes.ThemesPreferences
import com.example.githubappsv3.Model.Model
import com.example.githubappsv3.R
import com.example.githubappsv3.View.Adapter.UserAdapter
import com.example.githubappsv3.Themes.Factory.ThemesViewModelFactory
import com.example.githubappsv3.ViewModel.MainViewModel
import com.example.githubappsv3.ViewModel.ThemesViewModel
import com.example.githubappsv3.databinding.ActivityMainBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setTheme()
        prepareRecyclerView()
        prepareViewModel()
        viewModel.fetchingUser()
        binding.svUser.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            showLoading(true)
            viewModel.searchUser(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
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
            R.id.action_favorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.tvNotfound.visibility = View.GONE
            binding.rvUser.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE
        }
        else {
            binding.tvNotfound.visibility = View.GONE
            binding.rvUser.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun showNotFound() {
        binding.tvNotfound.visibility = View.VISIBLE
        binding.rvUser.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
    }

    private fun showError(t: Throwable?) {
        if (t != null) {
            Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
            t.printStackTrace()
        }
    }

    private fun prepareRecyclerView() {
        userAdapter = UserAdapter()
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = userAdapter.apply {
                onItemClickCallback = object :
                    UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Model.UserData) {
                        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                        intent.putExtra(DetailsActivity.USERNAME_EXTRA, data.login)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun prepareViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.getListUser().observe(this, Observer{ listUserResponse ->
            showLoading(false)
            if (listUserResponse !=null){
                if(listUserResponse.userList.isNotEmpty()){
                    userAdapter.setData(ArrayList(listUserResponse.userList))
                }else{
                    showNotFound()
                }
                showError(listUserResponse.t)
            }
        })

        viewModel.getResponse().observe(this, Observer { searchResponse ->
            showLoading(false)
            if (searchResponse != null) {
                if (searchResponse.userList.isNotEmpty()) {
                    userAdapter.setData(ArrayList(searchResponse.userList))
                } else {
                    showNotFound()
                }
                showError(searchResponse.t)
            }
        })
    }


    private fun setTheme(){

        val pref = ThemesPreferences.getInstance(dataStore)
        val themesViewModel = ViewModelProvider(this, ThemesViewModelFactory(pref)).get(
            ThemesViewModel::class.java
        )
        themesViewModel.getThemeSettings().observe(this,)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}