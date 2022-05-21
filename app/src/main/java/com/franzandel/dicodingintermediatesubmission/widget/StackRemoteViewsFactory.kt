package com.franzandel.dicodingintermediatesubmission.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.data.consts.IntentConst
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetStoriesUseCase
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeDetailMapper
import com.franzandel.dicodingintermediatesubmission.ui.splashscreen.SplashScreenActivity
import com.franzandel.dicodingintermediatesubmission.utils.GsonUtils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException

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

    }

    override fun onDataSetChanged() {
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

    override fun onDestroy() {

    }

    override fun getCount(): Int = stories.size

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)

        val futureTargetBitmap = Glide.with(context)
            .asBitmap()
            .load(stories[position].photoUrl)
            .submit(250, 250)
        try {
            remoteViews.setImageViewBitmap(R.id.imageView, futureTargetBitmap.get())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        val extras = bundleOf(
            IntentConst.EXTRA_STORY_DETAIL to GsonUtils.toJsonString(
                HomeDetailMapper.transform(stories[position])
            )
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        remoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews =
        RemoteViews(context.packageName, R.layout.layout_loading_widget)

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}
