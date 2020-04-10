package com.showtime.ui.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.sharedpreference.PreferenceManager

class SemesterListAdapter (
    val context: Context,
    var items:ArrayList<MyData.Semester>
): RecyclerView.Adapter<SemesterListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.semester_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val data = items[position]
        holder.semester.text = data.semester

        var pref = PreferenceManager(context)

        if(pref.table == position){
            holder.is_selected.visibility = VISIBLE
        } else {
            holder.is_selected.visibility = INVISIBLE
        }
        var total_credit = 0
        var lect_num = 0
        for(i in data.schedules){
            if(i.isLecture){
                total_credit += i.credit
                lect_num ++
            }
        }
        holder.credit.text = "${total_credit}학점"
        holder.lecture_count.text = "${lect_num}과목"

        holder.itemView.setOnClickListener {
//            var intent = Intent(context, DetailDialog::class.java)
//            intent.putExtra("tableNum", position)
//            context.startActivity(intent)
            var dialog = DetailDialog(context, position)
            dialog.show()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var semester: TextView
        var is_selected:ImageView
        var credit:TextView
        var lecture_count:TextView
        init {
            semester = itemView.findViewById(R.id.semester)
            is_selected = itemView.findViewById(R.id.is_selected)
            credit = itemView.findViewById(R.id.credit)
            lecture_count = itemView.findViewById(R.id.lecture_count)
        }

    }

}