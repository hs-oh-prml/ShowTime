package com.showtimetable.ui.dashboard

import android.animation.ValueAnimator
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.PointF
import android.graphics.Rect
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.CustomToast
import com.showtimetable.R
import com.showtimetable.data.MyData
import com.showtimetable.data.Schedule
import com.showtimetable.sharedpreference.PreferenceManager
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
    lateinit var ChangeContent:String

    val week = listOf("일", "월", "화", "수", "목", "금", "토")

    var touch_old=0f
    var touch_new=0f
    var disp_width=0
    var disp_height=0
    lateinit var mCenter:PointF
    var  mRadius:Float = 0f
    var mMaxDist:Float = 0f

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


        //calendar_frame.layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT,1000)

        setDisplaySize()
        imm = context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        pref = PreferenceManager(requireContext())

        var today = Calendar.getInstance()
        var listener = object: CalendarAdapter.calendarListener{


            override fun onClick(month:Int, year:Int, clickDate:Int, weekDay:String, v:View,check:Boolean) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                if(!check){
//
                    schedule_commit_btn.visibility = GONE
                    schedule_edit_text.visibility = GONE
                    schedule_delete_btn.visibility = INVISIBLE
                    schedule_content.visibility = VISIBLE
                    calendar_bottom.visibility = VISIBLE
                    arrow_image.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.down_arrow))
                    changeModeBar.setBackgroundColor(resources.getColor(R.color.light_blue))

                    var date:String = v.findViewById<TextView>(R.id.date).text.toString()
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
        }

        var adapter = CalendarAdapter(requireContext(), today, listener, false)
        //hi
        calendarView.adapter = adapter
        calendarView.setCurrentItem((Integer.MAX_VALUE) / 2, false)
            //
//        prev.setOnClickListener {
//            calendarView.setCurrentItem(calendarView.currentItem - 1, true)
//        }
//        next.setOnClickListener {
//            calendarView.setCurrentItem(calendarView.currentItem + 1, true)
//        }

        schedule_commit_btn.setOnClickListener {
            imm.hideSoftInputFromWindow(schedule_edit_text.windowToken, 0)
            calendar_frame.visibility = VISIBLE

            schedule_commit_btn.visibility = GONE
            pref.setDaySchedule(y, m, d, schedule_edit_text.text.toString())
            schedule_content.text = schedule_edit_text.text.toString()
            schedule_content.visibility = VISIBLE
            schedule_edit_text.visibility = GONE
            changeModeBar.visibility = VISIBLE
            write_schedule.visibility = GONE
            selected_date.visibility = VISIBLE

//hi
            var idx = calendarView.currentItem
            var adapter = CalendarAdapter(requireContext(), today, listener,false)
            calendarView.adapter = adapter
            calendarView.setCurrentItem(idx ,false)
            if(schedule_edit_text.text != null && schedule_edit_text.text.toString() != "" && ChangeContent != schedule_edit_text.text.toString()){
                val str = m.toString() + "월 "+ d + "일" + " 일정이 추가되었습니다."
                CustomToast(this.context!!, str).show()
            }
        }
        schedule_content.movementMethod = object: ScrollingMovementMethod(){
        }


        schedule_content.setOnClickListener {
            if(isSelected){
                schedule_date.text = selected_date.text.toString()
                schedule_commit_btn.visibility = VISIBLE
                calendar_frame.visibility = GONE
                schedule_delete_btn.visibility = GONE
                selected_date.visibility = GONE
                schedule_content.visibility = GONE
                schedule_edit_text.visibility = VISIBLE
                changeModeBar.visibility = GONE
                write_schedule.visibility = VISIBLE

            }
            ChangeContent = schedule_content.text.toString()
        }

        schedule_delete_btn.setOnClickListener {
            pref.setDaySchedule(y, m, d.toInt(),"")
            schedule_delete_btn.visibility = INVISIBLE

            var idx = calendarView.currentItem
            var adapter = CalendarAdapter(requireContext(), today, listener,false)
            calendarView.adapter = adapter
            calendarView.setCurrentItem(idx ,false)

            schedule_content.text = ""
            schedule_edit_text.setText("")
            schedule_delete_btn.visibility = VISIBLE
            changeModeBar.visibility = VISIBLE

            val str = m.toString() + "월 "+ d + "일" + " 일정이 삭제되었습니다."
            CustomToast(this.context!!, str).show()

        }

        close_write.setOnClickListener {
            calendar_frame.visibility = VISIBLE
            schedule_commit_btn.visibility = GONE
            schedule_content.visibility = VISIBLE
            schedule_edit_text.visibility = GONE
            changeModeBar.visibility = VISIBLE
            write_schedule.visibility = GONE
            selected_date.visibility = VISIBLE
        }

        changeModeBar.setOnClickListener {
            if(calendar_bottom.visibility == GONE){
                //내용세부화
                var idx = calendarView.currentItem
                var adapter = CalendarAdapter(requireContext(), today, listener,false)
                calendarView.adapter = adapter
                calendarView.setCurrentItem(idx ,false)

                arrow_image.setBackground(ContextCompat.getDrawable(this.context!!,R.drawable.down_arrow))
                changeModeBar.setBackgroundColor(resources.getColor(R.color.light_blue))
                calendar_bottom.visibility = VISIBLE
            }
            else{
                //동그라미만
                var idx = calendarView.currentItem
                var adapter = CalendarAdapter(requireContext(), today, listener,true)
                calendarView.adapter = adapter
                calendarView.setCurrentItem(idx ,false)

                calendar_bottom.visibility = GONE
                arrow_image.setBackground(ContextCompat.getDrawable(this.context!!,R.drawable.up_arrow))
                changeModeBar.setBackgroundColor(resources.getColor(R.color.white))
            }

        }

    }

    fun setDisplaySize(){
        var disp = DisplayMetrics()
        this.activity?.windowManager?.defaultDisplay?.getMetrics(disp)
        disp_width = disp.widthPixels
        disp_height = disp.heightPixels
        println("disp height "+disp_height)
    }

}