package com.dondika.storyapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dondika.storyapp.data.remote.stories.ListStoryItem
import com.dondika.storyapp.databinding.ActivityHomeBinding
import com.dondika.storyapp.ui.detail.DetailActivity
import com.dondika.storyapp.ui.upload.UploadStoryActivity
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.UserViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var storyAdapter: StoryAdapter

    private val homeViewModel: HomeViewModel by viewModels{
        UserViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllStories()
        setAdapter()
        setListener()

    }

    private fun setListener() {
        binding.fabCreateStory.setOnClickListener {
            startActivity(Intent(this, UploadStoryActivity::class.java))
        }
    }

    private fun getAllStories(){
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()
        homeViewModel.getAllStories(token)
        homeViewModel.storyResponse.observe(this){ result ->
            when(result){
                is Result.Loading -> {

                }
                is Result.Success -> result.data?.listStory?.let {
                    //homeAdapter.setData(it)
                    storyAdapter.submitList(it)
                }
                is Result.Error -> result.message?.let {

                }
            }

        }
    }

    private fun setAdapter(){
        storyAdapter = StoryAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(listStoryItem: ListStoryItem) {
                val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, listStoryItem)
                startActivity(intent)
            }
        })
    }


    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }


}