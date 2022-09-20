package com.franzandel.dicodingintermediatesubmission.ui.home.recyclerview

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/**
 * Created by Franz Andel
 * on 03 May 2022.
 */

class HomeLoadStateFooterAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<HomeLoadStateFooterViewHolder>() {

    override fun onBindViewHolder(holder: HomeLoadStateFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): HomeLoadStateFooterViewHolder {
        return HomeLoadStateFooterViewHolder.create(parent, retry)
    }
}

