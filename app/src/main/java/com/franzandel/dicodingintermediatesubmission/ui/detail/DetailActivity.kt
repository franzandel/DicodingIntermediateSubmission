package com.franzandel.dicodingintermediatesubmission.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityDetailBinding
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityHomeBinding
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyDetail = intent.extras?.getParcelable<StoryDetail>(HomeViewModel.EXTRA_STORY_DETAIL)
        binding.lifecycleOwner = this
        binding.storyDetail = storyDetail

        if (storyDetail?.lat != null && storyDetail.lon != null) {
            binding.tvLocation.isVisible = true
            // parse location name from lat lon
            // https://stackoverflow.com/a/9409229/9188214
        }
    }
    
}
