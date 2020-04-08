package com.showtime.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.Schedule
import com.showtime.sharedpreference.PreferenceManager

class DetailAdapter(
    val context: Context,
    var items:ArrayList<Schedule>
): RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.detail_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val data = items[position]
        holder.name.text = data.name
        if(data.isLecture){
            holder.credit.text = "${data.credit}학점"
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var credit:TextView
        init {
            name = itemView.findViewById(R.id.name)
            credit = itemView.findViewById(R.id.credit)
        }

    }

}