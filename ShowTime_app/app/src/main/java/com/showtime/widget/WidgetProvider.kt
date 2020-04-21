package com.showtime.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.GridLayout
import android.widget.GridView
import android.widget.RemoteViews
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import com.showtime.MainActivity
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.timetable.TableFragment
import kotlinx.android.synthetic.main.fragment_table.*
import java.lang.Exception

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

            val views = RemoteViews(context.packageName, R.layout.table_widget)
            var encodedStr = PreferenceManager(context).getImg()
            var img = str2Bitmap(encodedStr!!)
            if(img == null) {
                views.setViewVisibility(R.id.empty_view, VISIBLE)
                views.setViewVisibility(R.id.logo, VISIBLE)
                views.setViewVisibility(R.id.timeTable, GONE)
                views.setViewVisibility(R.id.logo_table, GONE)
            } else {
                views.setViewVisibility(R.id.logo, GONE)
                views.setViewVisibility(R.id.empty_view, GONE)
                views.setViewVisibility(R.id.timeTable, VISIBLE)
                views.setViewVisibility(R.id.logo_table, VISIBLE)
                views.setImageViewBitmap(R.id.timeTable, img)
            }

            var intent = Intent(context, MainActivity::class.java)
            var pi = PendingIntent.getActivity(context, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.widget_frame, pi)
            appWidgetManager.updateAppWidget(i, views)
            Log.d("Widget ID", i.toString())

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds)

    }

    fun str2Bitmap(encodedStr:String): Bitmap? {
        var encodeByte = Base64.decode(encodedStr, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        return bitmap
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}
