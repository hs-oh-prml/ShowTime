package com.showtime.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtime.data.MyData
import com.showtime.sharedpreference.PreferenceManager
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.showtime.R
import com.showtime.addschedule.AddScheduleActivity
import com.showtime.data.Schedule
import kotlinx.android.synthetic.main.dialog_detail.*


// 학기 클릭시 상세 정보 팝업
class DetailDialog(
    var c: Context,
    var tableNum:Int,
    var dListener:detailDialogDismissListener
) : DialogFragment() {

    lateinit var pref: PreferenceManager
    lateinit var info: MyData.Semester
    lateinit var data:ArrayList<Schedule>
    lateinit var adapter:DetailAdapter
    lateinit var listener:DetailAdapter.DetailListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.dialog_detail, container)
        return v
    }

    interface detailDialogDismissListener{
        fun onDismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    fun init(){

//        //다이얼로그 밖의 화면은 흐리게 만들어줌
//        val layoutParams = WindowManager.LayoutParams()
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
//        layoutParams.dimAmount = 0.8f

        pref = PreferenceManager(c)
        info = pref.myData.semester[tableNum]
        data = info.schedules
        Log.d("Detail Data", data.toString())
        var layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        listener = object: DetailAdapter.DetailListener{
            override fun refresh() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                init()
            }
        }

        adapter = DetailAdapter(c, data, tableNum, listener)
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dListener.onDismiss()
    }
}
