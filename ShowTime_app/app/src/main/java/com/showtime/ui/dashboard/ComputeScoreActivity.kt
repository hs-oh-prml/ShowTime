package com.showtime.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.showtime.R
import com.showtime.data.LectureItem
import com.showtime.data.MyData
import com.showtime.search.SearchActivity
import kotlinx.android.synthetic.main.activity_compute_score.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.recyclerView

class ComputeScoreActivity : AppCompatActivity() {
    lateinit var adapter:ComputeScoreAdapter
    lateinit var pref: SharedPreferences
    lateinit var myData:MyData
    lateinit var edit: SharedPreferences.Editor
    lateinit var scoreList:ArrayList<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compute_score)
        init()
    }
    fun computeScore():Double{
        // check
        for(i in scoreList){
            if(i == -2.0) {
                Toast.makeText(this, "성적을 입력하지 않은 과목이 있습니다.", Toast.LENGTH_SHORT).show()
                return 0.0
            }
        }
        var score = 0.0
        var credit = 0.0
        scoreList = adapter.getScore()
        for((index, i) in myData.fixList.withIndex()){
            if(scoreList[index] >= 0){
                score += scoreList[index] * (i.credit.toDouble())
                credit += i.credit.toDouble()
            }
        }
        return score / credit
    }
    fun init(){

        scoreList = ArrayList()
        pref = this.getSharedPreferences("myData", Activity.MODE_PRIVATE)
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
        Log.v("Compute", json)
        var credit = 0
        for(i in myData.fixList){
            credit += i.credit.toInt()
            scoreList.add(4.5)
        }
        var listener = object : ComputeScoreAdapter.scoreListener{
            override fun chcekScore() {
                var score = computeScore()
                score_view.text = "평점: %.2f".format(score)
            }
        }
        adapter = ComputeScoreAdapter(this, myData.fixList, scoreList, listener)
        var layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}
