package com.franzandel.dicodingintermediatesubmission.ui.home.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.franzandel.dicodingintermediatesubmission.domain.model.Story

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class HomeDiffCallback : DiffUtil.ItemCallback<Story>() {

    override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean =
        oldItem == newItem
}
