package com.franzandel.dicodingintermediatesubmission.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetStoriesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Franz Andel
 * on 14 May 2022.
 */

class StackRemoteViewsFactory(
    private val context: Context,
    private val getStoriesUseCase: GetStoriesUseCase,
    private val coroutineThread: CoroutineThread
) : RemoteViewsService.RemoteViewsFactory {

    private val stories = mutableListOf<Story>()

    override fun onCreate() {
        CoroutineScope(coroutineThread.main).launch {
            when (val result = getStoriesUseCase.execute()) {
                is Result.Success -> {
                    stories.addAll(result.data)
                }
                is Result.Error -> Toast.makeText(
                    context,
                    context.getString(R.string.system_error),
                    Toast.LENGTH_SHORT
                ).show()
                is Result.Exception -> Toast.makeText(
                    context,
                    context.getString(R.string.system_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDataSetChanged() {
        // insert image here
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = stories.size

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)
        Glide.with(context)
            .asBitmap()
            .load(stories[position].photoUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    remoteViews.setImageViewBitmap(R.id.imageView, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
//        rv.setImageViewBitmap(R.id.imageView, )
        val extras = bundleOf(
            StoryWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        remoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        // implement loading state here
//        TODO("Not yet implemented")
        return null
    }

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}
