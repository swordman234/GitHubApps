package com.example.githubapps.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapps.Model.Model
import com.example.githubapps.R
import com.example.githubapps.View.Adapter.UserAdapter
import com.example.githubapps.ViewModel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private lateinit var viewModel: FollowingViewModel
    private lateinit var followingAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = arguments?.getString(DetailsActivity.USERNAME_EXTRA)
        prepareRecyclerView()
        prepareViewModel()
        if (user != null) {
            showLoading(true)
            viewModel.fetchFollowing(user)
        }
    }

    private fun prepareRecyclerView() {
        followingAdapter = UserAdapter()
        rv_following.apply {
            layoutManager = LinearLayoutManager(this@FollowingFragment.context, RecyclerView.VERTICAL, false)
            adapter = followingAdapter.apply {
                onItemClickCallback = object :
                    UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Model.UserData) {
                        val intent = Intent(this@FollowingFragment.context, DetailsActivity::class.java)
                        intent.putExtra(DetailsActivity.USERNAME_EXTRA, data.login)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun prepareViewModel() {
        viewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
        viewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingResponse ->
            showLoading(false)
            if (followingResponse != null) {
                if (followingResponse.userList.isNotEmpty()) {
                    followingAdapter.setData(ArrayList(followingResponse.userList))
                } else {
                    showNotFound()
                }
                showError(followingResponse.t)
            }
        })
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            tv_notfound.visibility = View.GONE
            rv_following.visibility = View.GONE
            pb_loading.visibility = View.VISIBLE
        }
        else {
            tv_notfound.visibility = View.GONE
            rv_following.visibility = View.VISIBLE
            pb_loading.visibility = View.GONE
        }
    }

    private fun showNotFound() {
        tv_notfound.visibility = View.VISIBLE
        rv_following.visibility = View.GONE
        pb_loading.visibility = View.GONE
    }

    private fun showError(t: Throwable?) {
        if (t != null) {
            Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            t.printStackTrace()
        }
    }
}