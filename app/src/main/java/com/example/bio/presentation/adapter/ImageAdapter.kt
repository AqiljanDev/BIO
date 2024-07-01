package com.example.bio.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bio.R
import com.example.bio.databinding.ItemImageBinding
import com.example.bio.domain.entities.findOne.GalleryItem

class ImageAdapter(
    private val imageUrls: List<GalleryItem>
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Log.d("Mylog", "image adapter -- On bind View Holder. image: ${imageUrl.photo}")
        holder.bind(imageUrl.photo)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_view)

        fun bind(photo: String) {
            Glide.with(itemView.context)
                .load("http://192.168.8.3:4040/img/products/$photo")
                .into(imageView)
        }
    }
}


