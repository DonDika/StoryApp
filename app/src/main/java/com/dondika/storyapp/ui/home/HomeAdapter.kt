package com.dondika.storyapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dondika.storyapp.R
import com.dondika.storyapp.data.remote.stories.ListStoryItem
import com.dondika.storyapp.databinding.ItemStoryBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val storiesData = ArrayList<ListStoryItem>()

    fun setData(items: List<ListStoryItem>){
        storiesData.clear()
        storiesData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(storiesData[position])
    }

    override fun getItemCount(): Int {
        return storiesData.size
    }


    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storiesData: ListStoryItem){
            binding.apply {
                Glide.with(itemView).load(storiesData.photoUrl).into(imgStories)
                tvName.text = storiesData.name
                tvDesc.text = storiesData.description
                tvCreateDate.text = binding.root.resources.getString(R.string.created_add, storiesData.createdAt)

                //intent

            }
        }
    }

}