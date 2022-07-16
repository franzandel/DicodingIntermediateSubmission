package com.franzandel.dicodingintermediatesubmission.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.data.consts.IntentActionConst
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityHomeBinding
import com.franzandel.dicodingintermediatesubmission.ui.addstory.AddStoryActivity
import com.franzandel.dicodingintermediatesubmission.ui.detail.DetailActivity
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginActivity
import com.franzandel.dicodingintermediatesubmission.utils.extension.showDefaultSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val adapter by lazy { HomeAdapter(viewModel, this) }
    private val viewModel: HomeViewModel by viewModels()
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()
    private var isStoryAdded = false

    private val uploadImageActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                adapter.refresh()
                isStoryAdded = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.toolbar_home)
        initRV()
        initObservers()
        initListeners()
        initPaging3AdapterListener()
        viewModel.getStories()
        handleIntentAction()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        handleIntentAction()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu_logout -> {
                showLogoutConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                showDefaultSnackbar(getString(it.error), Snackbar.LENGTH_LONG)
            }
            binding.srlHome.isRefreshing = false
        }

        viewModel.navigateTo.observe(this) {
            startActivity(
                Intent(this, it.destination).putExtras(it.bundle),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        viewModel.clearStorageResult.observe(this) {
            if (it) {
                startActivity(
                    Intent(this, LoginActivity::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
                )
                finishAffinity()
            } else {
                showDefaultSnackbar(getString(R.string.system_error), Snackbar.LENGTH_SHORT)
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0 && isStoryAdded) {
                    binding.rvHome.smoothScrollToPosition(0)
                    isStoryAdded = false
                }
            }
        })
    }

    private fun initListeners() {
        binding.apply {
            srlHome.setOnRefreshListener {
                viewModel.getStories()
            }

            btnEmptyMessage.setOnClickListener {
                navigateToAddStory()
            }

            btnRetry.setOnClickListener {
                adapter.retry()
            }

            fabAddStory.setOnClickListener {
                navigateToAddStory()
            }
        }
    }

    private fun initPaging3AdapterListener() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.apply {
                    rvHome.isVisible = !isListEmpty && loadState.source.refresh !is LoadState.Error
                    pbHome.isVisible = loadState.source.refresh is LoadState.Loading
                    tvFailedMessage.isVisible = loadState.source.refresh is LoadState.Error
                    btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                    tvEmptyMessage.isVisible =
                        isListEmpty && loadState.source.refresh !is LoadState.Error
                    btnEmptyMessage.isVisible =
                        isListEmpty && loadState.source.refresh !is LoadState.Error
                }

                // Show snackbar on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    showDefaultSnackbar("\uD83D\uDE28 Wooops ${it.error}", Snackbar.LENGTH_LONG)
                }
            }
        }
    }

    private fun navigateToAddStory() {
        uploadImageActivityResultLauncher.launch(
            Intent(this@HomeActivity, AddStoryActivity::class.java),
            ActivityOptionsCompat.makeSceneTransitionAnimation(this@HomeActivity)
        )
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.home_logout_confirmation_title))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.clearStorage()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun handleIntentAction() {
        intent.action?.let { action ->
            val clazz = when (action) {
                IntentActionConst.NAVIGATE_TO_DETAIL -> DetailActivity::class.java
                IntentActionConst.NAVIGATE_TO_ADD_STORY -> AddStoryActivity::class.java
                else -> null
            }
            clazz?.let {
                startActivity(
                    Intent(this, it).apply {
                        intent.extras?.let { bundle ->
                            putExtras(bundle)
                        }
                    }
                )
            }
        }
    }
}
