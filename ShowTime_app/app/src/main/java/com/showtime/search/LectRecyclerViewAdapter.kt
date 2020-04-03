package com.showtime.search

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.LectureItem

class LectRecyclerViewAdapter (
    val context: Context,
    var items:ArrayList<LectureItem>,
    val listener:addListener
): RecyclerView.Adapter<LectRecyclerViewAdapter.ViewHolder>() {

    interface addListener{
        fun addLecture(lect:LectureItem)
        fun choiceLecture(lect:LectureItem)
        fun choiceCancel(lect: LectureItem)
    }
//
//    var lastData:LectureItem
//    init {
//        lastData = LectureItem("", "", "", "-1", "", "", "", "", "", "")
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_lecture, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        if (items == null)
            return 0
        return items.size
    }

    var lastSelectedPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

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

        if(position == lastSelectedPosition){
            holder.radio.isChecked = true
            holder.frame.setBackgroundResource(R.color.background_gray)
            holder.addBtn.visibility = VISIBLE

//            Log.d("last", lastData.toString())
//            if(lastData.lectNum != data.lectNum && lastData.lectNum != "-1"){
//                listener.choiceCancel(lastData)
//            }
            listener.choiceLecture(data)
//            lastData = data.copy()
//            Log.d("last2", lastData.toString())

        } else {
            holder.radio.isChecked = false
            holder.frame.setBackgroundResource(R.color.white)
            holder.addBtn.visibility = GONE
        }
        holder.addBtn.setOnClickListener {
            Log.v("Add Lecture", data.lectName)
            listener.addLecture(data)
//            lastData = LectureItem("", "", "", "-1", "", "", "", "", "", "")

        }
//
//        holder.itemView.setOnClickListener {
//            currentSelectedPosition = position
//            if(currentSelectedPosition != lastSelectedPosition){
//                holder.frame.setBackgroundResource(R.color.selected_item)
//                holder.addBtn.visibility = VISIBLE
//                notifyItemChanged(currentSelectedPosition)
//                lastSelectedPosition = currentSelectedPosition
//            } else {
//                holder.frame.setBackgroundResource(R.color.white)
//                holder.addBtn.visibility = GONE
//                lastSelectedPosition = -1
//                notifyItemChanged(currentSelectedPosition)
//            }
//
//        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var frame: LinearLayout
        var addBtn: TextView
        var radio:RadioButton

        var lectName:TextView
        var professor:TextView
        var timeNroom:TextView
        var grade:TextView
        var pobtDiv:TextView
        var credit:TextView
        var lectNum:TextView
        init {
            frame = itemView.findViewById(R.id.frame)
            addBtn = itemView.findViewById(R.id.add_btn)
            radio = itemView.findViewById(R.id.radioBtn)
            itemView.setOnClickListener {
                lastSelectedPosition = adapterPosition
                notifyDataSetChanged()
            }

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