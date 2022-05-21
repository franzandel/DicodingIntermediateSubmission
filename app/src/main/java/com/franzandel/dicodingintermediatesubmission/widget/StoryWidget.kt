package com.franzandel.dicodingintermediatesubmission.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.ui.splashscreen.SplashScreenActivity

/**
 * Implementation of App Widget functionality.
 */
class StoryWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.dicoding.picodiploma.TOAST_ACTION"
        const val EXTRA_ITEM = "com.dicoding.picodiploma.EXTRA_ITEM"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val serviceIntent = Intent(context, StackWidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            serviceIntent.data = serviceIntent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.story_widget)
            views.setRemoteAdapter(R.id.sv_stories, serviceIntent)
            views.setEmptyView(R.id.sv_stories, R.id.layout_empty_widget)

            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            // TODO: Need to handle navigation to AddStoryActivity either using deeplink or constants
            val splashScreenIntent = Intent(context, SplashScreenActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, splashScreenIntent, flags)
            views.setOnClickPendingIntent(R.id.btn_empty_message, pendingIntent)

            val toastIntent = Intent(context, StoryWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else 0
            )
            views.setPendingIntentTemplate(R.id.sv_stories, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
