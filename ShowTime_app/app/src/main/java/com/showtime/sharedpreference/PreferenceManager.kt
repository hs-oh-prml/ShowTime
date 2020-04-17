package com.showtime.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.data.Schedule
import com.showtime.data.TimeCell
import java.io.ByteArrayOutputStream

class PreferenceManager(c: Context) {
    val PREFERENCES_NAME = "My_Data"

    var pref: SharedPreferences
    var myData: MyData
    var edit: SharedPreferences.Editor
    var table:Int

    init {
        pref = c.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        edit = pref.edit()
        var gson = Gson()
        var json = pref.getString("myData", "")
        table = pref.getString("table", "0")!!.toInt()
        if(json != ""){
            myData = gson.fromJson(json, MyData::class.java)
        } else {
            var semester = ArrayList<MyData.Semester>()

            for(i in 0..7){
                var grade = i / 2 + 1
                var seme = i % 2 + 1
                var schedule = ArrayList<Schedule>()
                semester.add(MyData.Semester("${grade}학년 ${seme}학기",5, schedule))
            }
            myData = MyData(semester)
            edit.putString("table", "0")
        }
    }

    fun setAlarmFlag(flag:String){
        edit.putString("push", flag).commit()
    }
    fun getAlarmFlag():String? {
        return pref.getString("push","")
    }


    fun getDaySchedule(y:Int, m:Int, d:Int):String?{
        var date = "${y}-${m}-${d}"
        return pref.getString(date, "")
    }

    fun setDaySchedule(y:Int, m:Int, d:Int, content:String){
        var date = "${y}-${m}-${d}"
        edit.putString(date, content).commit()
    }


    fun getTheme():Int{
        return pref.getInt("theme", R.array.theme1)
    }

    fun setTheme(theme:Int){
        edit.putInt("theme", theme).commit()
    }

    fun getAlarmTime():String?{
        return pref.getString("alarm", "-1 23")
    }

    fun setAlarmTime(day:Int, hour:Int){
        edit.putString("alarm", "${day} ${hour}").commit()
    }


    fun setTableNum(t:Int){
        edit.putString("table", t.toString()).commit()
        table = t
    }

    fun Bitmap2Str(bitmap: Bitmap): String {
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        var byteArr = baos.toByteArray()
        var str = Base64.encodeToString(byteArr, Base64.DEFAULT)
        return str
    }

    fun saveImage(bitmap:Bitmap){
        var img = Bitmap2Str(bitmap)
        edit.putString("image",img)
        edit.commit()
    }
    fun getImg():String? {
        var img = pref.getString("image", "")
        return img
    }

    fun savePref(){
        var gson = Gson()
        var json = gson.toJson(myData)
        edit.putString("myData", json).commit()
    }

}