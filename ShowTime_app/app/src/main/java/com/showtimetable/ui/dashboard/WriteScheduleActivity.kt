package com.showtimetable.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.core.content.ContextCompat
import com.showtimetable.R
import kotlinx.android.synthetic.main.activity_write_schedule_activty.*

class WriteScheduleActivity : Activity() {

    val WRITE_ACTIVITY_CODE = 101
    val CANCEL_ACTIVITY_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_write_schedule_activty)
        onClick()
    }

    private fun onClick() {
        writeBtn.setOnClickListener {
            if (schedule_content.text.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("schedule", schedule_content.text.toString())
                val color = when (color_group.checkedRadioButtonId) {
                    color1.id -> "#dbc7fb"
                    color2.id -> "#c7ecd3"
                    color3.id -> "#c3dafc"
                    color4.id -> "#f7d1dc"
                    else -> "#f8ec9b"
                }
                intent.putExtra("color", color)
                setResult(WRITE_ACTIVITY_CODE, intent)
                finish()
            }

        }

        closeBtn.setOnClickListener {
            val intent = Intent()
            setResult(CANCEL_ACTIVITY_CODE, intent)
            finish()
        }
    }

}