package com.showtimetable.ui.dashboard

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
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
import com.showtimetable.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*

class DashboardFragment : Fragment() {

    lateinit var pref: PreferenceManager

    var y = 0
    var m = 0
    var d = 0

    var isSelected = false
    lateinit var imm: InputMethodManager

    var disp_width = 0
    var disp_height = 0
    lateinit var adapter: CalendarAdapter

    val WRITE_ACTIVITY_CODE = 101
    val CANCEL_ACTIVITY_CODE = 100

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

    fun init() {
        setDisplaySize()
        imm = context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        pref = PreferenceManager(requireContext())

        val today = Calendar.getInstance()
        y = today.get(Calendar.YEAR)
        m = today.get(Calendar.MONTH) + 1
        d = today.get(Calendar.DATE)
        val listener = object : CalendarAdapter.calendarListener {
            override fun onClick(
                month: Int,
                year: Int,
                clickDate: Int,
                weekDay: String,
                v: View,
                check: Boolean
            ) {
                if (!check) {
                    val date: String = v.findViewById<TextView>(R.id.date).text.toString()
                    y = year
                    m = month
                    d = date.toInt()
                    isSelected = true

                    if (clickDate != -1) {
                        val dateStr = "${d}. ${weekDay}"
                        selected_date.text = dateStr

                        // c_data: 같은 날의 일정 리스트
                        // CalendarData클래스의 CalendarItem 클래스 리스트
                        // CalendarItem: String Color, String Content
                        val c_data = pref.getDaySchedule(year, month, date.toInt()).calendarItemList

                        val listener = object : ContentListAdapter.Listener {
                            override fun onClick(pos: Int) {


                            }

                            override fun refresh() {
                                init()
                            }
                        }
                        val adapter =
                            ContentListAdapter(requireContext(), c_data, listener, y, m, d)
                        val layoutManager =
                            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                        schedule_recycler_view.layoutManager = layoutManager
                        schedule_recycler_view.adapter = adapter
                    }
                }
            }
        }

        adapter = CalendarAdapter(requireContext(), today, listener, false, y, m, d)
        calendarView.adapter = adapter
        calendarView.setCurrentItem((Integer.MAX_VALUE) / 2, false)

        schedule_content.movementMethod = object : ScrollingMovementMethod() {
        }


        add_date_schedule.setOnClickListener {
            if (isSelected) {
                val intent = Intent(requireContext(), WriteScheduleActivity::class.java)
                startActivityForResult(intent, WRITE_ACTIVITY_CODE)
            }
        }



        changeModeBar.setOnClickListener {
            if (calendar_bottom.visibility == GONE) {
                //내용세부화
                var idx = calendarView.currentItem
                adapter = CalendarAdapter(requireContext(), today, listener, false, y, m, d)
                calendarView.adapter = adapter
                calendarView.setCurrentItem(idx, false)
                calendar_bottom.visibility = VISIBLE
                arrow_image.setBackground(
                    ContextCompat.getDrawable(
                        context!!,
                        R.drawable.down_arrow
                    )
                )
            } else {
                //동그라미만
                var idx = calendarView.currentItem
                adapter = CalendarAdapter(requireContext(), today, listener, true, y, m, d)
                calendarView.adapter = adapter
                calendarView.setCurrentItem(idx, false)
                changeModeBar.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                calendar_bottom.visibility = GONE
                arrow_image.setBackground(ContextCompat.getDrawable(context!!, R.drawable.up_arrow))
            }

        }

    }

    private fun setDisplaySize() {
        val disp = DisplayMetrics()
        this.activity?.windowManager?.defaultDisplay?.getMetrics(disp)
        disp_width = disp.widthPixels
        disp_height = disp.heightPixels
        println("disp height " + disp_height)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WRITE_ACTIVITY_CODE) {
            if (resultCode == WRITE_ACTIVITY_CODE) {
                val schedule = data?.getStringExtra("schedule")!!
                val color = data?.getStringExtra("color")!!

                pref.addDaySchedule(y, m, d, schedule, color)

                val c_data = pref.getDaySchedule(y, m, d).calendarItemList
                val list_listener = object : ContentListAdapter.Listener {
                    override fun onClick(pos: Int) {
                    }

                    override fun refresh() {
                        init()
                    }
                }
                val listAdapter =
                    ContentListAdapter(requireContext(), c_data, list_listener, y, m, d)
                val layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                schedule_recycler_view.layoutManager = layoutManager
                schedule_recycler_view.adapter = listAdapter

                val str = m.toString() + "월 " + d + "일" + " 일정이 추가되었습니다."
                CustomToast(requireContext(), str).show()
                schedule_edit_text.text.clear()

            } else if (resultCode == CANCEL_ACTIVITY_CODE) {
                return
            }
        }

    }

}