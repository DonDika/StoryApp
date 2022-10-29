package com.dondika.storyapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dondika.storyapp.R
import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.databinding.ItemStoryBinding
import com.dondika.storyapp.utils.Utils

class HomeAdapter : PagingDataAdapter<StoryEntity, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storyData = getItem(position)
        if (storyData != null){
            holder.bind(storyData)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    inner class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyData: StoryEntity){
            binding.apply {
                Glide.with(itemView).load(storyData.photoUrl).into(imgStories)
                tvName.text = storyData.name
                tvDesc.text = storyData.description
                tvCreateDate.text = binding.root.resources.getString(R.string.created_add, Utils.formatDate(storyData.createdAt))

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(storyData)
                }
            }

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(storyData: StoryEntity)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryEntity> =
            object : DiffUtil.ItemCallback<StoryEntity>(){
                override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                    return oldItem.createdAt == newItem.createdAt
                }
                override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }



}