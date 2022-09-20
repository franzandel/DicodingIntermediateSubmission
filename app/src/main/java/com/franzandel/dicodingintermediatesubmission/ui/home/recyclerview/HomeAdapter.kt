package com.franzandel.dicodingintermediatesubmission.ui.home.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import com.franzandel.dicodingintermediatesubmission.databinding.ItemHomeBinding
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeViewModel

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class HomeAdapter(
    private val viewModel: HomeViewModel,
    private val lifecycleOwner: LifecycleOwner
) : PagingDataAdapter<Story, HomeViewHolder>(HomeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemHomeBinding =
            ItemHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ).apply {
                vm = viewModel
                lifecycleOwner = this@HomeAdapter.lifecycleOwner
            }

        return HomeViewHolder(itemHomeBinding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}
