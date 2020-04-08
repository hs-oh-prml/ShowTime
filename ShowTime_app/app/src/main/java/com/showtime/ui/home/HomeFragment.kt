package com.showtime.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.showtime.R
import com.showtime.timetable.TableFragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    fun init(){
        childFragmentManager.beginTransaction().replace(R.id.table, TableFragment(context!!, 0)).commit()
    }

    override fun onResume() {
        super.onResume()
        childFragmentManager.beginTransaction().replace(R.id.table, TableFragment(context!!, 0)).commit()

    }
}