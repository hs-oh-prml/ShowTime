package com.showtime.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.widget.NumberPicker
import android.widget.Toast
import com.github.mikephil.charting.utils.Utils.init
import com.showtime.R
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.widget.WidgetSettingActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    lateinit var pref: PreferenceManager
    val BIAS = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init()
    }
    fun init(){

        pref = PreferenceManager(this)
        push_switch.isChecked = pref.getAlarmFlag() == "true"

        d_day_alarm.minValue = 0
        d_day_alarm.maxValue = 5
        d_day_alarm.displayedValues = arrayOf("D-5", "D-4", "D-3", "D-2", "D-1", "D-Day")
        d_day_alarm.wrapSelectorWheel = false

        hour_alarm.minValue = 0
        hour_alarm.maxValue = 23
        hour_alarm.displayedValues = arrayOf("12 AM",
            "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM",
            "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM","6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM")
        var setAlarm = pref.getAlarmTime()!!.split(" ")
        Log.d("ALARM INFO", setAlarm.toString())
        d_day_alarm.value = setAlarm[0].toInt() + BIAS
        hour_alarm.value = setAlarm[1].toInt()
        Log.d("ALARM INFO", (setAlarm[0].toInt() + BIAS).toString())

        //푸시알람
        push_switch.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                pref.setAlarmFlag("true")
                Toast.makeText(this, "푸시 알람이 설정되었습니다.", Toast.LENGTH_SHORT).show()
//                alarm_detail.visibility = VISIBLE
            } else {
                pref.setAlarmFlag("false")
                Toast.makeText(this, "푸시 알람이 해제되었습니다.", Toast.LENGTH_SHORT).show()
//                alarm_detail.visibility = GONE
            }
        }
        //위젯설정
        setWidget.setOnClickListener {
            var intent = Intent(this, WidgetSettingActivity::class.java)
            startActivity(intent)
        }

        set_push_alarm.setOnClickListener {
            if(alarm_detail.visibility == VISIBLE){
                alarm_detail.visibility = GONE
            } else {
                alarm_detail.visibility = VISIBLE
            }
        }
        save_d_day.setOnClickListener {
            var d_day = d_day_alarm.value - BIAS
            var d_hour = hour_alarm.value
            Log.d("D_DAY_HOUR", "${d_day} ${d_hour}")
            pref.setAlarmTime(d_day, d_hour)
            alarm_detail.visibility = GONE

        }

        //테마변경
        setColorTheme.setOnClickListener {
            if(theme_detail.visibility == GONE){
                theme_detail.visibility = VISIBLE
            }else{
                theme_detail.visibility = GONE
            }
        }
        //테마선택시
        theme_group.setOnCheckedChangeListener { radioGroup, i ->

            when(i){
                R.id.theme1->{
                    pref.setTheme(R.array.theme1)
                }
                R.id.theme2->{
                    pref.setTheme(R.array.theme2)
                }
                else->{
                    pref.setTheme(R.array.theme3)
                }
            }
        }
        //뒤로가기
        setting_close.setOnClickListener {
            this.finish()
        }



    }
}
