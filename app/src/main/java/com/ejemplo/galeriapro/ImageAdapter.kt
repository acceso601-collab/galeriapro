package com.ejemplo.galeriapro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ejemplo.galeriapro.databinding.ItemImageBinding

class ImageAdapter(
    private val onItemClick: (imageUrl: String, sharedView: View) -> Unit
) : ListAdapter<String, ImageAdapter.ImageViewHolder>(DiffCallback) {

    private var showShimmer = true

    fun showShimmer(show: Boolean) {
        showShimmer = show
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = getItem(position)
        if (showShimmer) {
            holder.binding.imageView.setBackgroundResource(R.drawable.shimmer_background)
            holder.binding.imageView.load(null)
            holder.binding.badgeNew.visibility = View.GONE
        } else {
            holder.binding.imageView.setBackgroundResource(0)
            holder.binding.imageView.load(imageUrl) { crossfade(true) }
            holder.binding.badgeNew.visibility = if (position < 3) View.VISIBLE else View.GONE
        }
        holder.binding.imageView.transitionName = "shared_image_$position"
        holder.itemView.setOnClickListener {
            onItemClick(imageUrl, holder.binding.imageView)
        }
    }

    class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}
