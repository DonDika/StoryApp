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

    private lateinit var storyAdapter: StoryAdapter

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
        storyAdapter = StoryAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = storyAdapter
        }
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(listStoryItem: ListStoryItem) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, listStoryItem)
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
        }
    }

    private fun getAllStories(){
        val token = requireActivity().intent.getStringExtra(MainActivity.EXTRA_TOKEN).toString()
        homeViewModel.getAllStories(token)
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
        }
    }


}