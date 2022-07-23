package com.franzandel.dicodingintermediatesubmission.ui.home

import android.location.Geocoder
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ItemHomeBinding
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.utils.geolocation.GeolocationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class HomeViewHolder(private val itemHomeBinding: ItemHomeBinding) :
    RecyclerView.ViewHolder(itemHomeBinding.root) {

    fun bind(story: Story) {
        with(itemHomeBinding) {
            this.story = story
            setupLocation(lifecycleOwner, story)
        }
    }

    private fun setupLocation(lifecycleOwner: LifecycleOwner?, story: Story) {
        with(itemHomeBinding) {
            lifecycleOwner?.lifecycleScope?.launch(Dispatchers.Default) {
                if (story.latitude != null && story.longitude != null) {
                    val geocoder = Geocoder(root.context, Locale.getDefault())

                    val location = async {
                        GeolocationUtils.getCountryState(
                            geocoder,
                            story.latitude,
                            story.longitude
                        )
                    }

                    withContext(Dispatchers.Main) {
                        tvLocation.text = location.await()
                            ?: root.context.getString(R.string.failed_load_location)
                    }
                }
                withContext(Dispatchers.Main) {
                    tvLocation.isVisible = story.latitude != null && story.longitude != null
                }
            }
        }
    }
}
