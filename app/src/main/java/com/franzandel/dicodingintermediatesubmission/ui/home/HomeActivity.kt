package com.franzandel.dicodingintermediatesubmission.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityHomeBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val adapter by lazy { HomeAdapter(viewModel, this) }
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(applicationContext) }
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRV()
        initObservers()
        initListeners()
        initPaging3AdapterListener()
        viewModel.getStories()
    }

    private fun initRV() {
        binding.rvHome.adapter = this@HomeActivity.adapter.withLoadStateFooter(
            footer = HomeLoadStateFooterAdapter {
                adapter.retry()
            }
        )
    }

    private fun initObservers() {
        viewModel.homeResult.observe(this) {
            if (it.success != null) {
                lifecycleScope.launch(coroutineThread.background) {
                    adapter.submitData(it.success)
                }
            }
            if (it.error != null) {
                Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
            }
            binding.srlHome.isRefreshing = false
        }

        viewModel.navigateTo.observe(this) {
            startActivity(Intent(this, it.destination).putExtras(it.bundle))
        }
    }

    private fun initListeners() {
        binding.srlHome.setOnRefreshListener {
            viewModel.getStories()
        }

        binding.btnRetry.setOnClickListener {
            adapter.retry()
        }
    }

    private fun initPaging3AdapterListener() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.rvHome.isVisible = !isListEmpty && loadState.source.refresh !is LoadState.Error
                binding.pbHome.isVisible = loadState.source.refresh is LoadState.Loading
                binding.tvFailedMessage.isVisible = loadState.source.refresh is LoadState.Error
                binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        this@HomeActivity,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
