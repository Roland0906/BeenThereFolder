package com.example.beenthere.home.catogories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beenthere.R
import com.example.beenthere.data.Experience

//class ImageAdapter(private val context: Context, private val exp: Experience) :
class ImageAdapter(private val context: Context,
                   private val arrayList: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(arrayList[position]).into(holder.imageView)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClick(holder.imageView, arrayList[position])
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        onItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.list_item_image)
    }

    interface OnItemClickListener {
        fun onClick(imageView: ImageView, path: String)
    }
}
