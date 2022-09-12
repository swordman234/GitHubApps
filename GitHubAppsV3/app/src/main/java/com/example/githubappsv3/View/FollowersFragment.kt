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
import com.example.githubappsv3.ViewModel.FollowersViewModel
import com.example.githubappsv3.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: FollowersViewModel
    private lateinit var followersAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
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
            viewModel.fetchFollowers(user)
        }
    }

    private fun prepareRecyclerView() {
        followersAdapter = UserAdapter()
        binding.rvFollowers.apply {
            layoutManager = LinearLayoutManager(this@FollowersFragment.context, RecyclerView.VERTICAL, false)
            adapter = followersAdapter.apply {
                onItemClickCallback = object :
                    UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Model.UserData) {
                        val intent = Intent(this@FollowersFragment.context, DetailsActivity::class.java)
                        intent.putExtra(DetailsActivity.USERNAME_EXTRA, data.login)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun prepareViewModel() {
        viewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)
        viewModel.getFollowers().observe(viewLifecycleOwner, Observer { followersResponse ->
            showLoading(false)
            if (followersResponse != null) {
                if (followersResponse.userList.isNotEmpty()) {
                    followersAdapter.setData(ArrayList(followersResponse.userList))
                } else {
                    showNotFound()
                }
                showError(followersResponse.t)
            }
        })
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.tvNotfound.visibility = View.GONE
            binding.rvFollowers.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE
        }
        else {
            binding.tvNotfound.visibility = View.GONE
            binding.rvFollowers.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun showNotFound() {
        binding.tvNotfound.visibility = View.VISIBLE
        binding.rvFollowers.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
    }

    private fun showError(t: Throwable?) {
        if (t != null) {
            Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            t.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}