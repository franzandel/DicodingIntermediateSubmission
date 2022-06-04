package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.recyclerview.widget.RecyclerView
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
            this.story = story
        }
    }
}
