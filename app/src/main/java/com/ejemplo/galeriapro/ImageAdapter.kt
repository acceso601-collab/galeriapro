package com.ejemplo.galeriapro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ImageAdapter(private val onImageClick: (ImageModel) -> Unit) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val items = mutableListOf<ImageModel>()

    fun submitList(list: List<ImageModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgThumb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // Cargar imagen con tamaño pequeño para la cuadrícula
        holder.img.load("https://picsum.photos/id/${item.id}/400/400")
        holder.itemView.setOnClickListener { onImageClick(item) }
    }

    override fun getItemCount() = items.size
}
