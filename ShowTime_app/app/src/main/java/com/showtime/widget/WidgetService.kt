package com.showtime.widget
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.Log.v
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.Gravity.RIGHT
import android.view.View.GONE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.content.ContextCompat
import com.showtime.R
import com.showtime.sharedpreference.PreferenceManager

class WidgetService:RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.v("WidgetService", p0.toString())
        return TableRemoteViewFactory(this.applicationContext, p0!!)
    }
}


class TableRemoteViewFactory(
    var c: Context,
    var intent: Intent
): RemoteViewsService.RemoteViewsFactory
{
    var pref = PreferenceManager(c)
    var data = pref.myData.semester[pref.table]
    var widgetId = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID)

    var table = Array(23){Array(6) { "-1" } }
    var weekList = listOf("월", "화", "수", "목", "금")
    var color = c.resources.getStringArray(R.array.colorList6)
    var SIZE = 138

    override fun onCreate() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d("Widget", data.toString())
        for((index, i )in data.schedules.withIndex()){
            for(j in i.time){
                var flag = 0
                for(k in j.start .. j.end){
                    if(flag == 0){
                        table[k][j.week] = i.name + " ${index}"
                        flag = 1
                    } else {
                        table[k][j.week] = "${index}"
                    }
                }
            }
        }

    }

    override fun getLoadingView(): RemoteViews {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var rv = RemoteViews(c.packageName, R.layout.widget_loading)
        return rv
    }

    override fun getItemId(p0: Int): Long {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return p0.toLong()
    }

    override fun onDataSetChanged() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasStableIds(): Boolean {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun getViewAt(p0: Int): RemoteViews {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        Log.d("Widget_remote_view", p0.toString())
        var col = p0 % 6
        var row = p0 / 6
        var rv: RemoteViews
        rv = RemoteViews(c.packageName, R.layout.widget_item).apply{
            //            setTextViewText(R.id.widget_text, "testtestest")

            if(col != 0 && row == 0){
                setFloat(R.id.widget_text, "setTextSize", 11f)
                setInt(R.id.item_wrapper, "setGravity", CENTER)
                setTextViewText(R.id.widget_text, weekList[col - 1])
            }
            if(row != 0 ){
                if(row % 2 == 1){
//                    setInt(R.id.item_wrapper, "setMargins", 0)
                }
//                else {
//                    setInt(R.id.item_wrapper, "setMarginTop", 0)
//
//                }
            }
            if(col == 0 && row != 0){
                if(row % 2 == 1){
//                    setFloat(R.id.widget_text, "setTextSize", 10f)
//                    setInt(R.id.item_wrapper, "setWidth", WRAP_CONTENT)
//                    setInt(R.id.timeTable, "setColspan")
                    setInt(R.id.item_wrapper, "setGravity", CENTER)
                    if((9 + row / 2) > 12){
                        Log.d("WIDGET_TIME",((9 + row / 2) % 12).toString())
                        setTextViewText(R.id.widget_text,((9 + row / 2) % 12).toString())
                    } else {
                        Log.d("WIDGET_TIME",(9 + row / 2).toString())
                        setTextViewText(R.id.widget_text, (9 + row / 2).toString())
                    }
                }
            }
            if(col != 0 && row != 0){
                var info = table[row][col].split(" ")
                Log.v("WIDGET DATA INFO", info.toString())
                if(info.size > 1){
                    setTextViewText(R.id.widget_text, info[0])
                    setTextColor(R.id.widget_text, ContextCompat.getColor(c, R.color.white))
                    setInt(R.id.item_wrapper, "setBackgroundColor", Color.parseColor(color[info[1].toInt()]) )
                } else {
                    if(info[0] == "-1"){
                        setInt(R.id.item_wrapper, "setBackgroundColor", ContextCompat.getColor(c, R.color.white))
                    } else {
                        setTextColor(R.id.widget_text, ContextCompat.getColor(c, R.color.white))
//                        setInt(R.id.item_wrapper, "setVisibility", GONE )
                        setInt(R.id.item_wrapper, "setBackgroundColor", Color.parseColor(color[info[0].toInt()]) )
                    }
                }
            }
        }


        return rv
    }

    override fun getCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 138
    }

    override fun getViewTypeCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 1
    }

    override fun onDestroy() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}