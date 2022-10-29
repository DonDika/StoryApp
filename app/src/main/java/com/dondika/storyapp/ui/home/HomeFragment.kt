package com.dondika.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dondika.storyapp.R
import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.data.remote.stories.ListStoryItem
import com.dondika.storyapp.databinding.FragmentHomeBinding
import com.dondika.storyapp.ui.MainActivity
import com.dondika.storyapp.ui.detail.DetailActivity
import com.dondika.storyapp.ui.upload.UploadStoryActivity
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.UserViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyAdapter: HomeAdapter

    private val homeViewModel: HomeViewModel by viewModels {
        UserViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getAllStories()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        getAllStories()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setAdapter(){
        storyAdapter = HomeAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
        storyAdapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback{
            override fun onItemClicked(storyData: StoryEntity) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, storyData)
                startActivity(intent)
            }
        })

    }

    private fun setListener(){
        binding.fabCreateStory.setOnClickListener {
            startActivity(Intent(requireContext(), UploadStoryActivity::class.java))
        }
        binding.refreshStory.setOnRefreshListener {
            getAllStories()
            storyAdapter.refresh()
            binding.refreshStory.isRefreshing = false
        }
    }

    private fun getAllStories(){
        val token = requireActivity().intent.getStringExtra(MainActivity.EXTRA_TOKEN).toString()
        homeViewModel.fetchAllStories(token).observe(viewLifecycleOwner){
            storyAdapter.submitData(lifecycle, it)
        }

        /*homeViewModel.getAllStories(token)
        homeViewModel.storyResponse.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading ->{

                }
                is Result.Success -> result.data?.listStory?.let {
                    storyAdapter.submitList(it)
                }
                is Result.Error -> {

                }
            }
        }*/
    }


}