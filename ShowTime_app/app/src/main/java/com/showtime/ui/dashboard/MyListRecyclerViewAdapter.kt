package com.showtime.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.LectureItem
import kotlinx.android.synthetic.main.activity_search.view.*

class MyListRecyclerViewAdapter (
    val context: Context,
    var items:ArrayList<LectureItem>
): RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_my_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        if (items == null)
            return 0
        return items.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val data = items[position]
        holder.lectName.text = data.lectName
        holder.lectNum.text = data.lectNum
        var professorList = data.professor.split(",")
        var professor = ""
        for(i in professorList){
            professor += i.replace(" ", "") + " "
        }
        holder.professor.text = professor
        holder.credit.text = data.credit + "학점"
        var timeNroomList = data.timeNroom.split(",")
        var timeNroom = ""
        for(i in timeNroomList){
            timeNroom += i.replace(" ", "") + " "
        }
        holder.timeNroom.text = timeNroom
        holder.grade.text = data.grade + "학년"
        holder.pobtDiv.text= data.pobtDiv
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var lectName:TextView
        var professor:TextView
        var timeNroom:TextView
        var grade:TextView
        var pobtDiv:TextView
        var credit:TextView
        var lectNum:TextView
        init {
            lectName = itemView.findViewById(R.id.name)
            professor = itemView.findViewById(R.id.professor)
            timeNroom = itemView.findViewById(R.id.timeNroom)
            grade = itemView.findViewById(R.id.grade)
            pobtDiv = itemView.findViewById(R.id.pobtDiv)
            credit = itemView.findViewById(R.id.credit)
            lectNum = itemView.findViewById(R.id.lectNum)
        }
    }

}