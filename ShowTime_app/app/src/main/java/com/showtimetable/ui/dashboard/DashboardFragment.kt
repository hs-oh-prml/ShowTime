package com.showtimetable.ui.dashboard

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.PointF
import android.os.Bundle
import android.os.SystemClock
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.CustomToast
import com.showtimetable.R
import com.showtimetable.data.CalendarData
import com.showtimetable.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
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
    var mLastClickTime = 0L
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

        val today = Calendar.getInstance()
        val listener = object: CalendarAdapter.calendarListener{
            override fun onClick(month:Int, year:Int, clickDate:Int, weekDay:String, v:View, check:Boolean) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                if(calendar_bottom.visibility == GONE){
                    changeModeBar.performClick()
//                    this.onClick(m, y, clickDate, weekDay, v, check)
                } else {
                    if(!check){
                        val date:String = v.findViewById<TextView>(R.id.date).text.toString()
                        y = year
                        m = month
                        d = date.toInt()
                        isSelected = true

                        if(clickDate != -1){
                            val dateStr = "${d}. ${weekDay}"
                            selected_date.text = dateStr

                            // c_data: 같은 날의 일정 리스트
                            // CalendarData클래스의 CalendarItem 클래스 리스트
                            // CalendarItem: String Color, String Content
                            var c_data = pref.getDaySchedule(year, month, date.toInt()).calendarItemList
//                        schedule_edit_text.setText(null)
//                        c_data = ArrayList<CalendarData.CalendarItem>()
//                        if(c_data == null){
//                        }
                            var listener = object : ContentListAdapter.Listener{
                                override fun onClick(pos: Int) {
//                                    TODO("Not yet implemented")
//                                schedule_edit_text.setText(c_data[pos].content)
//                                    schedule_delete_btn.visibility = VISIBLE
                                }
                            }
                            var adapter = ContentListAdapter(requireContext(), c_data, listener)
                            var layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                            schedule_recycler_view.layoutManager = layoutManager
                            schedule_recycler_view.adapter = adapter
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
//            schedule_content.visibility = VISIBLE
            schedule_edit_text.visibility = GONE
            changeModeBar.visibility = VISIBLE
            write_schedule.visibility = GONE
            selected_date.visibility = VISIBLE
            schedule_recycler_view.visibility = VISIBLE

//hi
            var idx = calendarView.currentItem
            var adapter = CalendarAdapter(requireContext(), today, listener,false)
            calendarView.adapter = adapter
            calendarView.setCurrentItem(idx ,false)
            if(schedule_edit_text.text != null && schedule_edit_text.text.toString() != ""){
                var color = ""
                var c_str = schedule_edit_text.text.toString()
                Log.d("Content", c_str)
                when(color_group.checkedRadioButtonId){
                    R.id.color1-> color = "#dbc7fb"
                    R.id.color2-> color = "#c7ecd3"
                    R.id.color3-> color = "#c3dafc"
                    R.id.color4-> color = "#f7d1dc"
                    R.id.color5-> color = "#f8ec9b"
                    else -> color = "#dbc7fb"
                }

                pref.addDaySchedule(y, m, d, c_str, color)

                var c_data = pref.getDaySchedule(y, m, d).calendarItemList
                var list_listener = object : ContentListAdapter.Listener{
                    override fun onClick(pos: Int) {
//                                    TODO("Not yet implemented")
//                        schedule_edit_text.setText(c_data[pos].content)

//                        schedule_delete_btn.visibility = VISIBLE
                    }
                }
                var listAdapter = ContentListAdapter(requireContext(), c_data, list_listener)
                var layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                schedule_recycler_view.layoutManager = layoutManager
                schedule_recycler_view.adapter = listAdapter

                val str = m.toString() + "월 "+ d + "일" + " 일정이 추가되었습니다."
                CustomToast(requireContext(), str).show()
                schedule_edit_text.text.clear()
            }
        }
        schedule_content.movementMethod = object: ScrollingMovementMethod(){
        }


        add_date_schedule.setOnClickListener {
            if(isSelected){
                schedule_date.text = selected_date.text.toString()
                schedule_commit_btn.visibility = VISIBLE
                calendar_frame.visibility = GONE
//                schedule_delete_btn.visibility = GONE
                selected_date.visibility = GONE
//                schedule_content.visibility = GONE
                schedule_edit_text.visibility = VISIBLE
                changeModeBar.visibility = GONE
                write_schedule.visibility = VISIBLE
                schedule_recycler_view.visibility = GONE
                var c_data = ArrayList<CalendarData.CalendarItem>()
                var list_listener = object : ContentListAdapter.Listener{
                    override fun onClick(pos: Int) {
//                                    TODO("Not yet implemented")
//                        schedule_edit_text.setText(c_data[pos].content)
//                        schedule_delete_btn.visibility = VISIBLE
                    }
                }
                var listAdapter = ContentListAdapter(requireContext(), c_data, list_listener)
                var layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                schedule_recycler_view.layoutManager = layoutManager
                schedule_recycler_view.adapter = listAdapter
            }
            ChangeContent = schedule_content.text.toString()
        }

        schedule_delete_btn.setOnClickListener {
            pref.deleteDaySchedule(y, m, d.toInt(), 0)
            schedule_delete_btn.visibility = INVISIBLE
            var idx = calendarView.currentItem
            var adapter = CalendarAdapter(requireContext(), today, listener,false)
            calendarView.adapter = adapter
            calendarView.setCurrentItem(idx ,false)

            schedule_content.text = ""
            schedule_edit_text.text.clear()
            schedule_delete_btn.visibility = VISIBLE
            changeModeBar.visibility = VISIBLE

            val str = m.toString() + "월 "+ d + "일" + " 일정이 삭제되었습니다."
            CustomToast(requireContext(), str).show()

        }

        close_write.setOnClickListener {
            calendar_frame.visibility = VISIBLE
            schedule_commit_btn.visibility = GONE
//            schedule_content.visibility = VISIBLE
            schedule_edit_text.visibility = GONE
            changeModeBar.visibility = VISIBLE
            write_schedule.visibility = GONE
            selected_date.visibility = VISIBLE
            schedule_recycler_view.visibility = VISIBLE
        }

        changeModeBar.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 100){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            if(calendar_bottom.visibility == GONE){
                //내용세부화
                var idx = calendarView.currentItem
                var adapter = CalendarAdapter(requireContext(), today, listener,false)
                calendarView.adapter = adapter
                calendarView.setCurrentItem(idx ,false)

                arrow_image.setBackground(ContextCompat.getDrawable(context!!, R.drawable.down_arrow))
                changeModeBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
                calendar_bottom.visibility = VISIBLE
                Log.d("CLICK ON", calendar_bottom.visibility.toString())
            }
            else{
                //동그라미만
                var idx = calendarView.currentItem
                var adapter = CalendarAdapter(requireContext(), today, listener,true)
                calendarView.adapter = adapter
                calendarView.setCurrentItem(idx ,false)
                arrow_image.setBackground(ContextCompat.getDrawable(context!!, R.drawable.up_arrow))
                changeModeBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                calendar_bottom.visibility = GONE
                Log.d("CLICK OFF", calendar_bottom.visibility.toString())

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