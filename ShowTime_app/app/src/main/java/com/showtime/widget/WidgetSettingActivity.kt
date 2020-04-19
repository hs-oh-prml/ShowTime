package com.showtime.widget

import android.Manifest
import android.animation.ObjectAnimator
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.fragment.app.FragmentActivity
import com.showtime.CustomToast
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.data.Schedule
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.timetable.TableFragment
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.activity_widget_setting.*
import kotlinx.android.synthetic.main.activity_widget_setting.timeTable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.semester_item.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class WidgetSettingActivity : FragmentActivity() {

    lateinit var pref:PreferenceManager

    lateinit var semester: MyData.Semester
    lateinit var color: Array<String>
    lateinit var weekList:List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_setting)
        init()
    }


    fun init(){
        pref = PreferenceManager(this)
        weekList = listOf("월", "화", "수", "목", "금")
        semester = pref.myData.semester[pref.table]
        var theme = pref.getTheme()
        color = this.resources.getStringArray(theme)

        var thread = object: Thread(){
            override fun run() {
                super.run()
                sleep(1000)

                runOnUiThread {
                    initView(weekList)
                    refreshTable()
                    widget_setting_btn.isEnabled = true
                }
            }
        }
        thread.start()

        widget_setting_btn.setOnClickListener {
            val currentDegree = widget_setting_btn.rotation
            ObjectAnimator.ofFloat(widget_setting_btn, View.ROTATION, currentDegree, currentDegree + 360f)
                .setDuration(1000)
                .start()
            screenCapture()
            //preview.setImageBitmap(str2Bitmap(pref.getImg()!!))
            val str = "현재 시간표로 변경되었습니다."
            CustomToast(this, str).show()
        }

        setting_close.setOnClickListener {
            this.finish()
        }

    }
    fun screenCapture(){

        // Make Bitmap By Captured View
        var bitmap = Bitmap.createBitmap(timeTable.width, timeTable.height, Bitmap.Config.ARGB_8888)
        Log.v("IMAGE SIZE", "${timeTable.width}, ${timeTable.height}")
        var canvas = Canvas(bitmap)
        timeTable.draw(canvas)
        pref.saveImage(bitmap)
    }

    fun str2Bitmap(encodedStr:String): Bitmap {
        var encodeByte = Base64.decode(encodedStr, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        return bitmap
    }

    fun refreshTable(){
        for(i in 1..22){
            for(j in 1..weekList.size){
                var cell = getChild(i, j)
                cell.setBackgroundResource(R.color.white)
            }
        }

        for((index, i) in  semester.schedules.withIndex()){
            addTable(i, index)
        }
    }
    fun getChild(row:Int, col:Int): View {
        var index = (timeTable.columnCount * row) + col
        return timeTable.getChildAt(index)
    }

    fun addTable(schedule: Schedule, index:Int){
        var time = schedule.time
        for(i in time){
            var flag = 0
            for(j in i.start..i.end){

                var cell = timeTable.getChildAt((timeTable.rowCount * i.week) + j)
                var cell_name = cell.findViewById<TextView>(R.id.cell_name)
                var cell_place =  cell.findViewById<TextView>(R.id.cell_place)

                if(flag == 0){
                    val name = schedule.name
                    var place = ""
                    if(schedule.place != null){
                        place = schedule.place
                    }
                    cell_name.setPadding(3, 3, 0, 0)
                    cell_place.setPadding(3, 0, 0, 0)
                    cell_name.text = name
                    cell_place.text = place
                    cell_name.textSize = 7f
                    cell_place.textSize = 6f

                    val shape = GradientDrawable()
                    shape.setColor(Color.parseColor(color[index % color.size]))
                    shape.shape = GradientDrawable.RECTANGLE
                    shape.cornerRadius = 15.0f
                    cell.background = shape

                    var param = GridLayout.LayoutParams()
                    var rowSpan = GridLayout.spec(j, (i.end - i.start + 1), GridLayout.FILL)
                    var colSpan = GridLayout.spec(i.week, 1, GridLayout.FILL)
                    param.rowSpec = rowSpan
                    param.columnSpec = colSpan
                    cell.layoutParams = param

                } else {
                    cell.visibility = View.GONE
                }
                flag++
            }

        }
    }

    fun initView(weekList: List<String>){

        timeTable.columnCount = weekList.size + 1
        timeTable.rowCount = 23

        var f_height = timeTable.height
        var f_width = timeTable.width

        var flag = true
        for(j in 0 until timeTable.columnCount){
            for(i in 0 until timeTable.rowCount){

                var params = GridLayout.LayoutParams()
                if(!flag){
                    params.height = (f_height / 23.5).toInt()

                }
                params.setMargins(1)
                var child: View
                var colSpan = GridLayout.spec(j, GridLayout.FILL)
                var rowSpan = GridLayout.spec(i, GridLayout.FILL)

                params.columnSpec = colSpan
                params.rowSpec = rowSpan

                if(i == 0 && j == 0){
                    child = TextView(this)
                    child.setTextColor(ContextCompat.getColor(this, R.color.white))
                    child.textSize = 7f

                    f_width -= child.width
                    f_height -= child.height
                    flag = false
                } else if(i == 0 && j != 0){

                    var colSpan = GridLayout.spec(j, GridLayout.FILL, 1f)
                    params.columnSpec = colSpan
                    child = TextView(this)
                    child.gravity = Gravity.CENTER
                    child.textSize = 7f
                    child.text = weekList[j - 1]
                    params.height = child.height

                } else if(i != 0 && j == 0){

                    child = TextView(this)
                    child.gravity = Gravity.TOP or Gravity.RIGHT
                    child.textSize = 7f
                    child.includeFontPadding = false
                    child.height = WRAP_CONTENT
                    params.height = child.height
                    if(i % 2 == 1){
                        if((9 + i / 2) > 12){
                            child .text = ((9 + i / 2) % 12).toString()
                        } else {
                            child .text = (9 + i / 2).toString()
                        }
                    }
                } else {
                    var inflater = LayoutInflater.from(this)
                    child = inflater.inflate(R.layout.table_item, timeTable, false)
                }

                if(i % 2 == 1){
                    params.setMargins(1,1,1,0)
                } else {
                    if(i == 0){
                        params.setMargins(1,1,1,1)
                    }else {
                        params.setMargins(1,0,1,1)
                    }
                }
                child.setBackgroundResource(R.color.white)
                child.layoutParams = params

                timeTable.addView(child)
            }
        }
    }

}
