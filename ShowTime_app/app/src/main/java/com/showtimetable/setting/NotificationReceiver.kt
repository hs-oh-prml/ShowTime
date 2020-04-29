package com.showtimetable.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.showtimetable.MainActivity
import com.showtimetable.R
import com.showtimetable.sharedpreference.PreferenceManager
import java.util.*

class NotificationReceiver():BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        notificationBuild(p0!!)
    }
    fun notificationBuild(context:Context){

        var pref = PreferenceManager(context)

        var alarmInfo = pref.getAlarmTime()
        var list = alarmInfo!!.split(" ")

        var hasSchedule = Calendar.getInstance()
        hasSchedule.add(Calendar.DATE, -(list[0].toInt()))

        var y= hasSchedule.get(Calendar.YEAR)
        var m= hasSchedule.get(Calendar.MONTH) + 1
        var d = hasSchedule.get(Calendar.DATE)
        var str = pref.getDaySchedule(y, m, d)


        var noti_intent = Intent(context, MainActivity::class.java)
        var pi = PendingIntent.getActivity(context, 0, noti_intent, PendingIntent.FLAG_ONE_SHOT)

        if(str != ""){
            var builder = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(str)
                .setContentTitle("일정")
                .setContentIntent(pi)
                .setAutoCancel(true)
            var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                var channel = NotificationChannel("default", "schedule_alert", NotificationManager.IMPORTANCE_DEFAULT)
                    .apply {
                        setShowBadge(false)
                    }
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(1, builder.build())
        }
    }
}