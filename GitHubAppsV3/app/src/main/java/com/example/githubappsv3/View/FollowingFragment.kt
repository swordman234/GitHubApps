package com.example.githubappsv3.View

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
import com.example.githubappsv3.Model.Model
import com.example.githubappsv3.View.Adapter.UserAdapter
import com.example.githubappsv3.ViewModel.FollowingViewModel
import com.example.githubappsv3.databinding.FragmentFollowingBinding


class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: FollowingViewModel
    private lateinit var followingAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
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
        binding.rvFollowing.apply {
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
            binding.tvNotfound.visibility = View.GONE
            binding.rvFollowing.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE
        }
        else {
            binding.tvNotfound.visibility = View.GONE
            binding.rvFollowing.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun showNotFound() {
        binding.tvNotfound.visibility = View.VISIBLE
        binding.rvFollowing.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
    }

    private fun showError(t: Throwable?) {
        if (t != null) {
            Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            t.printStackTrace()
        }
    }
}