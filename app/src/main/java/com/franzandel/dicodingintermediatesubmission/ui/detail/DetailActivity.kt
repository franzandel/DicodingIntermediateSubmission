package com.franzandel.dicodingintermediatesubmission.ui.detail

import android.location.Geocoder
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityDetailBinding
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeViewModel
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModels { DetailViewModelFactory() }

    private val storyDetail: StoryDetail? by lazy {
        intent.extras?.getParcelable(HomeViewModel.EXTRA_STORY_DETAIL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.toolbar_detail)
        initDataBinding()
        initLocationUI()
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = this
        binding.storyDetail = storyDetail
        binding.vm = viewModel
    }

    private fun initLocationUI() {
        storyDetail?.latitude?.let { latitude ->
            storyDetail?.longitude?.let { longitude ->
                val geocoder = Geocoder(this, Locale.getDefault())
                viewModel.getLocation(geocoder, latitude, longitude)
            }
        }
    }
}
