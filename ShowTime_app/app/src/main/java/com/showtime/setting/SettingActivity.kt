package com.showtime.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        push_switch.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                pref.setAlarmFlag("true")
                Toast.makeText(this, "푸시 알람이 설정되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                pref.setAlarmFlag("false")
                Toast.makeText(this, "푸시 알람이 해제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        setWidget.setOnClickListener {
            var intent = Intent(this, WidgetSettingActivity::class.java)
            startActivity(intent)
        }
        setColorTheme.setOnClickListener {

        }
    }
}
