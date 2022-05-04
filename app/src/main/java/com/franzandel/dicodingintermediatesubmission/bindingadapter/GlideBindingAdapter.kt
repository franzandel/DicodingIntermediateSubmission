package com.franzandel.dicodingintermediatesubmission.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.franzandel.dicodingintermediatesubmission.R

/**
 * Created by Franz Andel
 * on 04 May 2022.
 */

@BindingAdapter("glideUrl")
fun bindGlideImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(url)
            .placeholder(R.drawable.ic_baseline_broken_image_24)
            .into(view)
    }
}
