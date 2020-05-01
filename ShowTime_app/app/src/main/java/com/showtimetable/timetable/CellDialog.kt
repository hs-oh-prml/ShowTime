package com.showtimetable.timetable

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import androidx.fragment.app.DialogFragment
import com.showtimetable.DetailAdapter
import com.showtimetable.DetailDialog
import com.showtimetable.MainActivity
import com.showtimetable.R
import com.showtimetable.data.MyData
import com.showtimetable.data.Schedule
import com.showtimetable.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.dialog_cell.*

class CellDialog(
    var c: Context,
    var tableNum:Int,
    var index:Int
) : DialogFragment() {

    lateinit var pref: PreferenceManager
    lateinit var info: MyData.Semester
    lateinit var data:Schedule
    lateinit var adapter: DetailAdapter
    lateinit var listener: DetailAdapter.DetailListener
    val weekList = listOf("월", "화", "수", "목", "금")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.dialog_cell, container)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        val display = activity!!.windowManager.defaultDisplay // in case of Fragment
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        init()
    }

    fun init(){

//        //다이얼로그 밖의 화면은 흐리게 만들어줌
//        val layoutParams = WindowManager.LayoutParams()
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
//        layoutParams.dimAmount = 0.8f

        pref = PreferenceManager(c)
        info = pref.myData.semester[tableNum]
        data = info.schedules[index]
        name.text = data.name
        if(data.place == "")
            place.visibility = GONE
        else
            place.text = "장소 : ${data.place}"
        if(data.isLecture){
            credit.text = "학점 : ${data.credit}학점"
        }
        var time_ = ""
        for(i in data.time){
            var sHalfTime = "00"
            if(i.start % 2 == 0){
                sHalfTime = "30"
            }
            var eHalfTime = "00"
            if(i.end % 2 == 1){
                eHalfTime = "30"
            }
            time_ += weekList[i.week - 1] + " ${((i.start - 1)  / 2) + 9}:${sHalfTime} - ${i.end / 2 + 9}:${eHalfTime} "
        }
        time.text = time_
        Log.d("cell", data.toString())

        delete.setOnClickListener {
            pref.myData.semester[tableNum].schedules.removeAt(index)
            pref.savePref()
            dismiss()
            var intent = Intent(context, MainActivity::class.java)
            activity!!.finish()
            startActivity(intent)
        }
    }

}
