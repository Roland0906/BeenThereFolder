package com.example.beenthere

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        Glide.with(imgView.context) // GlideApp error
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.icons_36px_home_normal)
                    .error(R.drawable.icons_36px_home_normal)
            )
            .into(imgView)
    }
}