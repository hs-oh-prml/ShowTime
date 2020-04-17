package com.showtime.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.*
import android.widget.Toast
import com.github.mikephil.charting.utils.Utils.init
import com.showtime.R
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.widget.WidgetSettingActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    lateinit var pref: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init()
    }
    fun init(){

        pref = PreferenceManager(this)
        push_switch.isChecked = pref.getAlarmFlag() == "true"
        //푸시알람
        if(push_switch.isChecked){
            alarm_detail.visibility = VISIBLE
        }
        push_switch.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                pref.setAlarmFlag("true")
                Toast.makeText(this, "푸시 알람이 설정되었습니다.", Toast.LENGTH_SHORT).show()
                alarm_detail.visibility = VISIBLE
            } else {
                pref.setAlarmFlag("false")
                Toast.makeText(this, "푸시 알람이 해제되었습니다.", Toast.LENGTH_SHORT).show()
                alarm_detail.visibility = GONE
            }
        }
        //위젯설정
        setWidget.setOnClickListener {
            var intent = Intent(this, WidgetSettingActivity::class.java)
            startActivity(intent)
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

                }
                R.id.theme2->{

                }
                else->{

                }
            }
        }
        //뒤로가기
        setting_close.setOnClickListener {
            this.finish()
        }

    }
}
