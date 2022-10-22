package com.dondika.storyapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dondika.storyapp.R
import com.dondika.storyapp.data.remote.stories.ListStoryItem
import com.dondika.storyapp.databinding.ItemStoryBinding
import com.dondika.storyapp.utils.Utils

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storiesData = getItem(position)
        holder.bind(storiesData)
    }

    inner class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storiesData: ListStoryItem){
            //Log.e("CEK BIND", "$storiesData" )
            binding.apply {
                Glide.with(itemView)
                    .load(storiesData.photoUrl)
                    .into(imgStories)
                tvName.text = storiesData.name
                tvDesc.text = storiesData.description
                tvCreateDate.text = binding.root.resources.getString(R.string.created_add, Utils.formatDate(storiesData.createdAt))

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(storiesData)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(listStoryItem: ListStoryItem)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>(){
                override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                    return oldItem.createdAt == newItem.createdAt
                }
                override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                    return oldItem == newItem
                }
            }
    }

}