package com.showtime.ui.dashboard

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_dashboard.*

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
        pref = PreferenceManager(requireContext())
        var layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        var adapter = SemesterListAdapter(requireContext(), pref.myData.semester)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter


    }

}