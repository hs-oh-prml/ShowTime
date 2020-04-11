package com.showtime.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.GridView
import android.widget.RemoteViews
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.timetable.TableFragment
import kotlinx.android.synthetic.main.fragment_table.*

/**
 * Implementation of App Widget functionality.
 */
class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        Log.d("Widget Array", appWidgetIds.toString())
        for(i in appWidgetIds){

            var intent = Intent(context, WidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, i)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val views = RemoteViews(context.packageName, R.layout.table_widget)
            views.setRemoteAdapter(R.id.timeTable, intent)
            views.setEmptyView(R.id.timeTable, R.id.empty_view)

            appWidgetManager.updateAppWidget(i, views)
            Log.d("Widget ID", i.toString())

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds)

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}
