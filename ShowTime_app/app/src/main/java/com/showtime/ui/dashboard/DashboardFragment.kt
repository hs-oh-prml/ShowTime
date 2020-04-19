package com.showtime.ui.dashboard

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtime.CustomToast
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.data.Schedule
import com.showtime.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.calendar_item.*
import kotlinx.android.synthetic.main.calendar_layout.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.w3c.dom.Text
import java.util.*

class DashboardFragment : Fragment() {

    lateinit var pref: PreferenceManager

    var y = 0
    var m = 0
    var d = 0

    var isSelected = false
    lateinit var imm:InputMethodManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    fun init(){
        imm = context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        pref = PreferenceManager(requireContext())

        var today = Calendar.getInstance()
        var listener = object: CalendarAdapter.calendarListener{
            override fun onClick(month:Int, year:Int, clickDate:Int, weekDay:String, v:View) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                schedule_commit_btn.visibility = GONE
                schedule_content.visibility = VISIBLE
                schedule_edit_text.visibility = GONE
                schedule_delete_btn.visibility = INVISIBLE

                var date = v.findViewById<TextView>(R.id.date).text.toString()
                y = year
                m = month
                d = date.toInt()
                isSelected = true

                if(clickDate != -1){
                    selected_date.text = "${d}. " + weekDay
                    var str = pref.getDaySchedule(year, month, date.toInt())
                    //println("content : "+str)
                    if(str != null && str != ""){
                        schedule_content.text = str
                        schedule_edit_text.setText(str)
                        schedule_delete_btn.visibility = VISIBLE
                    }
                    else{
                        schedule_content.text = null
                        schedule_edit_text.setText(null)
                    }
                }
            }
        }

        var adapter = CalendarAdapter(requireContext(), today, listener)
        calendarView.adapter = adapter
        calendarView.setCurrentItem((Integer.MAX_VALUE) / 2, false)

        prev.setOnClickListener {
            calendarView.setCurrentItem(calendarView.currentItem - 1, true)
        }
        next.setOnClickListener {
            calendarView.setCurrentItem(calendarView.currentItem + 1, true)
        }

        schedule_commit_btn.setOnClickListener {
            imm.hideSoftInputFromWindow(schedule_edit_text.windowToken, 0)
            calendar_frame.visibility = VISIBLE

            schedule_commit_btn.visibility = GONE
            pref.setDaySchedule(y, m, d, schedule_edit_text.text.toString())
            schedule_content.text = schedule_edit_text.text.toString()
            schedule_content.visibility = VISIBLE
            schedule_edit_text.visibility = GONE
            edit_layout2.visibility = VISIBLE

            var idx = calendarView.currentItem
            var adapter = CalendarAdapter(requireContext(), today, listener)
            calendarView.adapter = adapter
            calendarView.setCurrentItem(idx ,false)
            if(schedule_edit_text.text != null && schedule_edit_text.text.toString() != ""){
                val str = m.toString() + "월 "+ d + "일" + " 일정이 추가되었습니다."
                CustomToast(this.context!!, str).show()
            }
        }

        edit_layout.setOnClickListener {
            if(isSelected){
                schedule_commit_btn.visibility = VISIBLE
                edit_layout2.visibility = GONE
                calendar_frame.visibility = GONE
                schedule_delete_btn.visibility = INVISIBLE
                schedule_content.visibility = GONE
                schedule_edit_text.visibility = VISIBLE
            }
        }

        schedule_content.setOnClickListener {
            if(isSelected){
                schedule_commit_btn.visibility = VISIBLE
                edit_layout2.visibility = GONE
                calendar_frame.visibility = GONE
                schedule_delete_btn.visibility = INVISIBLE
                schedule_content.visibility = GONE
                schedule_edit_text.visibility = VISIBLE
            }
        }

        edit_layout2.setOnClickListener {
            if(isSelected){
                schedule_commit_btn.visibility = VISIBLE
                edit_layout2.visibility = GONE
                calendar_frame.visibility = GONE
                schedule_delete_btn.visibility = INVISIBLE
                schedule_content.visibility = GONE
                schedule_edit_text.visibility = VISIBLE
            }
        }

//        schedule_close_btn.setOnClickListener {
//            imm.hideSoftInputFromWindow(schedule_edit_text.windowToken, 0)
//            calendar_frame.visibility = VISIBLE
//
//            schedule_commit_btn.visibility = GONE
//            schedule_edit_btn.visibility = VISIBLE
//
//            schedule_content.visibility = VISIBLE
//            schedule_edit_text.visibility = GONE
//            schedule_close_btn.visibility = GONE
//        }

        schedule_delete_btn.setOnClickListener {
            pref.setDaySchedule(y, m, d.toInt(),"")
            schedule_delete_btn.visibility = INVISIBLE

            var idx = calendarView.currentItem
            var adapter = CalendarAdapter(requireContext(), today, listener)
            calendarView.adapter = adapter
            calendarView.setCurrentItem(idx ,false)

            schedule_content.text = ""
            schedule_edit_text.setText("")
            schedule_delete_btn.visibility = VISIBLE

            val str = m.toString() + "월 "+ d + "일" + " 일정이 삭제되었습니다."
            CustomToast(this.context!!, str).show()

        }
    }

}