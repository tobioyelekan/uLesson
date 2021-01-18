package com.example.ulesson.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String) {
    if (url.isNotEmpty()) {
        Glide.with(context)
            .load(url)
            .into(this)
    }
}