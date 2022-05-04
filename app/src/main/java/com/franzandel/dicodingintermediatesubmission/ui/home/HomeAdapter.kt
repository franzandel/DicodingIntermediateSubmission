package com.franzandel.dicodingintermediatesubmission.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.franzandel.dicodingintermediatesubmission.databinding.ItemHomeBinding
import com.franzandel.dicodingintermediatesubmission.domain.model.Story

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class HomeAdapter(private val viewModel: HomeViewModel) :
    PagingDataAdapter<Story, HomeViewHolder>(HomeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemHomeBinding =
            ItemHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ).apply {
                vm = viewModel
            }

        return HomeViewHolder(itemHomeBinding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}
