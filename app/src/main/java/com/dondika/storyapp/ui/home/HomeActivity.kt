package com.dondika.storyapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dondika.storyapp.R
import com.dondika.storyapp.databinding.ActivityHomeBinding
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.UserViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter

    private val homeViewModel: HomeViewModel by viewModels{
        UserViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllStories()
        setAdapter()

    }

    private fun getAllStories(){
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()
        homeViewModel.getAllStories(token)
        homeViewModel.storyResponse.observe(this){ result ->
            when(result){
                is Result.Loading -> {

                }
                is Result.Success -> result.data?.listStory?.let {
                    homeAdapter.setData(it)
                }
                is Result.Error -> result.message?.let {

                }
            }

        }
    }

    private fun setAdapter(){
        homeAdapter = HomeAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = homeAdapter
        }
    }



    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }


}