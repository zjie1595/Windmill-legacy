package com.zj.windmill.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zj.windmill.databinding.ItemVideoVerticalBinding
import com.zj.windmill.model.Video

object VideoComparator : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return false
    }
}

class SearchPagingAdapter(
    private val onItemClick: (Video) -> Unit
) :
    PagingDataAdapter<Video, SearchPagingAdapter.VideoViewHolder>(VideoComparator) {

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            ItemVideoVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding, onItemClick)
    }

    inner class VideoViewHolder(
        private val binding: ItemVideoVerticalBinding,
        private val onItemClick: (Video) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.m = video
            binding.root.setOnClickListener {
                onItemClick(video)
            }
            binding.executePendingBindings()
        }
    }
}