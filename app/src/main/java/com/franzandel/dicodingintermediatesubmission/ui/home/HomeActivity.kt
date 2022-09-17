package com.franzandel.dicodingintermediatesubmission.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
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
import com.franzandel.dicodingintermediatesubmission.data.consts.IntentActionConst
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityHomeBinding
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import com.franzandel.dicodingintermediatesubmission.ui.addstory.AddStoryActivity
import com.franzandel.dicodingintermediatesubmission.ui.detail.DetailActivity
import com.franzandel.dicodingintermediatesubmission.ui.detail.StoryDetail
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginActivity
import com.franzandel.dicodingintermediatesubmission.utils.extension.showDefaultSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var coroutineThread: CoroutineThread

    private lateinit var binding: ActivityHomeBinding

    private val adapter by lazy { HomeAdapter(viewModel, this) }
    private val viewModel: HomeViewModel by viewModels()
    private var isStoryAdded = false
    private var isLocationSpecificChosen = false
    private var menu: Menu? = null

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
        viewModel.getLocationPreference()
        handleIntentAction()
        EspressoIdlingResource.increment()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        handleIntentAction()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        this.menu = menu
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
            R.id.menu_location_preference -> {
                item.isChecked = !item.isChecked
                isLocationSpecificChosen = item.isChecked

                val locationPreference = if (isLocationSpecificChosen) {
                    ONLY_WITH_LOCATION_STORIES
                } else {
                    ALL_STORIES
                }
                viewModel.getStories(locationPreference)
                viewModel.setLocationPreference(locationPreference)
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

        viewModel.navigateToDetail.observe(this) {
            startActivity(
                DetailActivity.newIntent(this, it),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        viewModel.clearStorageResult.observe(this) {
            if (it) {
                startActivity(
                    LoginActivity.newIntent(this),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
                )
                finishAffinity()
            } else {
                showDefaultSnackbar(getString(R.string.system_error), Snackbar.LENGTH_SHORT)
            }
        }

        viewModel.locationPreference.observe(this) {
            isLocationSpecificChosen = it == ONLY_WITH_LOCATION_STORIES
            menu?.findItem(R.id.menu_location_preference)?.isChecked = isLocationSpecificChosen
            viewModel.getStories(it)
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
                val locationPreference = if (isLocationSpecificChosen) {
                    ONLY_WITH_LOCATION_STORIES
                } else {
                    ALL_STORIES
                }
                viewModel.getStories(locationPreference)
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

                if (loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                    EspressoIdlingResource.decrement()
                } else if (!isListEmpty && loadState.source.refresh !is LoadState.Error && adapter.itemCount != 0) {
                    EspressoIdlingResource.decrement()
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
            val intent = when (action) {
                IntentActionConst.NAVIGATE_TO_DETAIL -> {
                    val storyDetail =
                        intent.extras?.getParcelable<StoryDetail?>(DetailActivity.EXTRA_STORY_DETAIL)
                    DetailActivity.newIntent(this, storyDetail)
                }
                IntentActionConst.NAVIGATE_TO_ADD_STORY -> AddStoryActivity.newIntent(this)
                else -> null
            }
            intent?.let {
                startActivity(it)
            }
        }
    }

    companion object {
        const val ALL_STORIES = 0
        private const val ONLY_WITH_LOCATION_STORIES = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
