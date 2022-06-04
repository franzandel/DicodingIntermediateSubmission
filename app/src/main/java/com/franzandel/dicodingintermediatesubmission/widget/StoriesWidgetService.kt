package com.franzandel.dicodingintermediatesubmission.widget

import android.content.Intent
import android.widget.RemoteViewsService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 14 May 2022.
 */

@AndroidEntryPoint
class StoriesWidgetService : RemoteViewsService() {

    @Inject
    lateinit var storiesRemoteViewsFactory: StoriesRemoteViewsFactory

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = storiesRemoteViewsFactory
}
