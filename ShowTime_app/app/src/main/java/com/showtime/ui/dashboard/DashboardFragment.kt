package com.showtime.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.data.Schedule
import com.showtime.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.calendar_layout.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import java.util.*

class DashboardFragment : Fragment() {
    lateinit var pref: PreferenceManager
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
//        pref = PreferenceManager(requireContext())
//        var layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
//        var adapter = SemesterListAdapter(requireContext(), pref.myData.semester)
//        recycler_view.layoutManager = layoutManager
//        recycler_view.adapter = adapter

        var today = Calendar.getInstance()
        Log.d("Fragment_Calendar", today.get(Calendar.YEAR).toString())
        Log.d("Fragment_Calendar", today.get(Calendar.MONTH).toString())
        Log.d("Fragment_Calendar", today.get(Calendar.DATE).toString())
        Log.d("Fragment_Calendar", today.get(Calendar.DAY_OF_WEEK).toString())
        Log.d("Fragment_Calendar", today.getActualMaximum(Calendar.DAY_OF_MONTH).toString())

        var adapter = CalendarAdapter(requireContext(), today)
        calendarView.adapter = adapter
        calendarView.setCurrentItem((Integer.MAX_VALUE) / 2, false)

        prev.setOnClickListener {
            calendarView.setCurrentItem(calendarView.currentItem - 1, true)
        }
        next.setOnClickListener {
            calendarView.setCurrentItem(calendarView.currentItem + 1, true)
        }
    }

}