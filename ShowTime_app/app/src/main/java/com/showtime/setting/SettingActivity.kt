package com.showtime.setting

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.showtime.CustomToast
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
        d_day_alarm.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        hour_alarm.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
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
                val str = "푸시 알람을 설정합니다."
                CustomToast(this, str).show()
                alarm_detail.visibility = VISIBLE
            } else {
                pref.setAlarmFlag("false")
                val str = "푸시 알람을 해제합니다."
                CustomToast(this, str).show()
                alarm_detail.visibility = GONE
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
                    val str = "테마1 로 변경되었습니다."
                    CustomToast(this, str).show()
                }
                R.id.theme2->{
                    pref.setTheme(R.array.theme2)
                    val str = "테마2 로 변경되었습니다."
                    CustomToast(this, str).show()
                }
                else->{
                    pref.setTheme(R.array.theme3)
                    val str = "테마3 로 변경되었습니다."
                    CustomToast(this, str).show()
                }
            }
        }
        //뒤로가기
        setting_close.setOnClickListener {
            this.finish()
        }

        makeColor()
    }



    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }

    fun makeColor(){
        val layout1 = findViewById<LinearLayout>(R.id.theme_layout1)
        val layout2 = findViewById<LinearLayout>(R.id.theme_layout2)
        val layout3 = findViewById<LinearLayout>(R.id.theme_layout3)

        val t1 = this.resources.getStringArray(R.array.theme1)
        for(i in 0..t1.size-1){
            val view = layout1.getChildAt(i)
            val color = t1[i]
            view.setBackgroundColor(Color.parseColor(color))
        }

        val t2 = this.resources.getStringArray(R.array.theme2)
        for(i in 0..t2.size-1){
            val view = layout2.getChildAt(i)
            val color = t2[i]
            view.setBackgroundColor(Color.parseColor(color))
        }

        val t3 = this.resources.getStringArray(R.array.theme3)
        for(i in 0..t3.size-1){
            val view = layout3.getChildAt(i)
            val color = t3[i]
            view.setBackgroundColor(Color.parseColor(color))
        }

    }
}
