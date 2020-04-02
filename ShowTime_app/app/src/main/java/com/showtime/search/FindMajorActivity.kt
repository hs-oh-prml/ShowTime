package com.showtime.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.MajorItem
import com.showtime.database.DBHelper
import kotlinx.android.synthetic.main.activity_find_major.*

class FindMajorActivity : AppCompatActivity() {

    var dbHelper = DBHelper(this)
    lateinit var adapter: MajorRecyclerViewAdapter
    lateinit var listener: MajorRecyclerViewAdapter.MajorAdapterListener
    lateinit var departList:ArrayList<MajorItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_major)
        init()
    }

    fun init(){
        var originData = dbHelper.getMajor()

        departList = ArrayList()
        for(i in originData){
            departList.add(i[0])
            Log.v("Depart", i[0].toString())
        }

        var layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = layoutManager

        listener = object: MajorRecyclerViewAdapter.MajorAdapterListener{
            override fun itemOnClick(position: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                var list =  ArrayList<MajorItem>()
                for(i in originData[position]){
                    list.add(MajorItem(i.depart, i.major, i.code, i.type))
                }

                list[0].type = 1
                Log.v("Change List", list.toString())
                adapter = MajorRecyclerViewAdapter(applicationContext, list, this)
                recycler_view.adapter = adapter

                arrow_right.visibility = VISIBLE
                depart.visibility = VISIBLE
                depart.text = list[0].depart

            }

            override fun selectMajor(item: MajorItem) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("Selected Major", item.major)
                var intent = Intent()
                intent.putExtra("major", item.major)
                intent.putExtra("major_code",item.code)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        adapter = MajorRecyclerViewAdapter(this, departList, listener)
        recycler_view.adapter = adapter


        depart_list.setOnClickListener {
            arrow_right.visibility = INVISIBLE
            depart.visibility = INVISIBLE
            adapter = MajorRecyclerViewAdapter(this, departList, listener)
            recycler_view.adapter = adapter
        }

    }

    override fun onBackPressed() {
        if(depart.visibility == VISIBLE){
            arrow_right.visibility = INVISIBLE
            depart.visibility = INVISIBLE
            adapter = MajorRecyclerViewAdapter(this, departList, listener)
            recycler_view.adapter = adapter
        } else {
            super.onBackPressed()
        }
    }
}
