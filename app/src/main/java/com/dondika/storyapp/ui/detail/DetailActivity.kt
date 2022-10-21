package com.dondika.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dondika.storyapp.R
import com.dondika.storyapp.data.remote.stories.ListStoryItem
import com.dondika.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDetail()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setDetail(){
        val userData = intent.getParcelableExtra<ListStoryItem>(EXTRA_USER)
        if (userData != null){
            binding.apply {
                Glide.with(this@DetailActivity).load(userData.photoUrl).into(imgStories)

                tvName.text = userData.name
                tvDesc.text = userData.description
                tvCreateDate.text = binding.root.resources.getString(R.string.created_add, userData.createdAt)

                supportActionBar!!.title = userData.name
            }
        }
    }


    companion object {
        const val EXTRA_USER = "extra_user"
    }

}