package com.zj.windmill.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import com.zj.windmill.databinding.ItemHomeBannerBinding
import com.zj.windmill.model.Video

class HomeBannerAdapter(
    videos: List<Video>,
    private val onItemClick: (Video) -> Unit
) : BannerAdapter<Video, HomeBannerAdapter.BannerViewHolder>(videos) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding =
            ItemHomeBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding, onItemClick)
    }

    override fun onBindView(holder: BannerViewHolder, data: Video, position: Int, size: Int) {
        holder.bind(data)
    }

    inner class BannerViewHolder(
        private val binding: ItemHomeBannerBinding,
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