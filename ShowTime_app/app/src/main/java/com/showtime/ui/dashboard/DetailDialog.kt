package com.showtime.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtime.data.MyData
import com.showtime.sharedpreference.PreferenceManager
import android.view.WindowManager
import com.showtime.addschedule.AddScheduleActivity
import kotlinx.android.synthetic.main.dialog_detail.*


// 학기 클릭시 상세 정보 팝업
class DetailDialog(
    var c: Context,
    var tableNum:Int
) : Dialog(c) {
    lateinit var pref: PreferenceManager
    lateinit var info: MyData.Semester
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        window!!.attributes = layoutParams

        setContentView(com.showtime.R.layout.dialog_detail)
//        requestWindowFeature(FEATURE_NO_TITLE)

        init()
    }

    fun init(){

        pref = PreferenceManager(c)
        info = pref.myData.semester[tableNum]
        var layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        var adapter = DetailAdapter(c, info.schedules, tableNum)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter

        title.text = info.semester
        var _credit = 0
        var _count = 0
        for(i in info.schedules){
            if(i.isLecture){
                _count++
                _credit += i.credit
            }
        }
        total_credit.text = "${_credit}학점"
        lecture_count.text = "${_count} 과목"

        close.setOnClickListener {
            dismiss()
        }
        select.setOnClickListener {
            pref.setTableNum(tableNum)
            dismiss()
        }
        add.setOnClickListener {
            var intent = Intent(c, AddScheduleActivity::class.java)
            intent.putExtra("tableNum", tableNum)
            c.startActivity(intent)
        }
    }

}
