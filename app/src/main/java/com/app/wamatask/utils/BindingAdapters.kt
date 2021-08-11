package com.app.wamatask.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.app.wamatask.R
import com.bumptech.glide.Glide


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("photoUrl")
    fun setPhotoUrl(imageView: ImageView, glideUrl: String) {
        if(!glideUrl.isNullOrEmpty())
        {
            Glide.with(imageView.context)
                .load(glideUrl)
                .placeholder(R.drawable.default_placeholder)
                .error(R.drawable.default_placeholder)
                .into(imageView)
        }
    }

}

