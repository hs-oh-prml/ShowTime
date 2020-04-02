package com.showtime.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.showtime.R
import com.showtime.data.LectureItem
import com.showtime.data.MyData
import com.showtime.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    lateinit var adapter:MyListRecyclerViewAdapter
    lateinit var pref: SharedPreferences
    lateinit var myData:MyData
    lateinit var edit: SharedPreferences.Editor

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
        pref = context!!.getSharedPreferences("myData", Activity.MODE_PRIVATE)
        edit = pref.edit()
        var gson = Gson()
        var json = pref.getString("myData", "")
        if(json != ""){
            myData = gson.fromJson(json, MyData::class.java)
        } else {
            var fixList = ArrayList<LectureItem>()
            var tempList = ArrayList<LectureItem>()
            myData = MyData(fixList, tempList)
        }
        Log.d("myData", myData.toString())
        adapter = MyListRecyclerViewAdapter(context!!, myData.fixList)
        var layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        addLect.setOnClickListener {
            var intent = Intent(context!!, SearchActivity::class.java)
            startActivity(intent)
        }
        computeScore.setOnClickListener {
            var intent = Intent(context!!, ComputeScoreActivity::class.java)
            startActivity(intent)

        }
    }
}