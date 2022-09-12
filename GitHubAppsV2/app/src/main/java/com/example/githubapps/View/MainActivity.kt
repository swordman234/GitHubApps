package com.example.githubapps.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapps.Model.Model
import com.example.githubapps.R
import com.example.githubapps.View.Adapter.UserAdapter
import com.example.githubapps.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareRecyclerView()
        prepareViewModel()
        viewModel.fetchingUser()
        sv_user.setOnQueryTextListener(this)
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            tv_notfound.visibility = View.GONE
            rv_user.visibility = View.GONE
            pb_loading.visibility = View.VISIBLE
        }
        else {
            tv_notfound.visibility = View.GONE
            rv_user.visibility = View.VISIBLE
            pb_loading.visibility = View.GONE
        }
    }

    private fun showNotFound() {
        tv_notfound.visibility = View.VISIBLE
        rv_user.visibility = View.GONE
        pb_loading.visibility = View.GONE
    }

    private fun showError(t: Throwable?) {
        if (t != null) {
            Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
            t.printStackTrace()
        }
    }

    private fun prepareRecyclerView() {
        userAdapter = UserAdapter()
        rv_user.apply {
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
}