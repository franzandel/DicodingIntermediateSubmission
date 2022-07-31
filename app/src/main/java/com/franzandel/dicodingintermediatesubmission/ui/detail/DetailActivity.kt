package com.franzandel.dicodingintermediatesubmission.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityDetailBinding
import com.franzandel.dicodingintermediatesubmission.ui.maps.MapsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModels()

    private val storyDetail: StoryDetail? by lazy {
        intent.extras?.getParcelable(EXTRA_STORY_DETAIL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.toolbar_detail)
        initDataBinding()
        initToolbar()
        initLocationUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (storyDetail?.latitude != null && storyDetail?.longitude != null) {
            menuInflater.inflate(R.menu.menu_detail, menu)
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_map -> {
                storyDetail?.latitude?.let { latitude ->
                    storyDetail?.longitude?.let { longitude ->
                        startActivity(
                            MapsActivity.newIntent(this, latitude, longitude),
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
                        )
                    }
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = this
        binding.storyDetail = storyDetail
        binding.vm = viewModel
    }

    private fun initToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initLocationUI() {
        storyDetail?.latitude?.let { latitude ->
            storyDetail?.longitude?.let { longitude ->
                viewModel.getLocation(this, latitude, longitude)
            }
        }
    }

    companion object {
        const val EXTRA_STORY_DETAIL = "extra_story_detail"

        fun newIntent(context: Context, storyDetail: StoryDetail?): Intent {
            return Intent(context, DetailActivity::class.java).run {
                putExtra(EXTRA_STORY_DETAIL, storyDetail)
            }
        }
    }
}
