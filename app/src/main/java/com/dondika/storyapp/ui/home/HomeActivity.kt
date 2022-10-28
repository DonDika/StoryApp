package com.dondika.storyapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dondika.storyapp.R
import com.dondika.storyapp.data.remote.stories.ListStoryItem
import com.dondika.storyapp.databinding.ActivityHomeBinding
import com.dondika.storyapp.ui.detail.DetailActivity
import com.dondika.storyapp.ui.upload.UploadStoryActivity
import com.dondika.storyapp.ui.user.login.LoginActivity
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

    override fun onResume() {
        super.onResume()
        getAllStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_sign_out -> {
                homeViewModel.deleteUser()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
                true
            }
            R.id.setting_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getAllStories(){
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()
        homeViewModel.getAllStories(token)
        homeViewModel.storyResponse.observe(this){ result ->
            when(result){
                is Result.Loading -> {
                    onLoading(true)
                }
                is Result.Success -> result.data?.listStory?.let {
                    onLoading(false)
                    storyAdapter.submitList(it)
                }
                is Result.Error ->  {
                    onLoading(false)
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

    private fun setListener() {
        binding.fabCreateStory.setOnClickListener {
            startActivity(Intent(this, UploadStoryActivity::class.java))
        }
        binding.refreshStory.setOnRefreshListener {
            getAllStories()
        }
    }

    private fun onLoading(isLoading: Boolean){
        binding.refreshStory.isRefreshing = isLoading
    }

    private fun onFailed(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }

}