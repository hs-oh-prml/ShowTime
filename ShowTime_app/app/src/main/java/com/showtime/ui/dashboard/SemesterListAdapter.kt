package com.showtime.ui.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.MyData

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
        init {
            semester = itemView.findViewById(R.id.semester)
        }

    }

}