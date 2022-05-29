package com.franzandel.dicodingintermediatesubmission.data.model

import androidx.annotation.Keep
import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse

@Keep
data class HomeResponse(
    val listStory: List<StoryResponse>
): BaseResponse()
