package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ItemHomeBinding
import com.franzandel.dicodingintermediatesubmission.domain.model.Story

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class HomeViewHolder(private val itemHomeBinding: ItemHomeBinding) :
    RecyclerView.ViewHolder(itemHomeBinding.root) {

    fun bind(story: Story) {
        with(itemHomeBinding) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .into(ivStory)

            tvName.text = story.name
            tvDescription.text = story.description
            tvTime.text = story.createdAt

            root.setOnClickListener {

            }
        }
    }
}
