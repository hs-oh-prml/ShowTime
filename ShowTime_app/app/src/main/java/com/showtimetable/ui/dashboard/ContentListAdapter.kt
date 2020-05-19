package com.showtimetable.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.R
import com.showtimetable.data.CalendarData

class ContentListAdapter(
    var context: Context,
    var itemList:ArrayList<CalendarData.CalendarItem>,
    var listener:Listener
): RecyclerView.Adapter<ContentListAdapter.ViewHolder>() {

    interface Listener{
        fun onClick(pos:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.calendar_schedule_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val data = itemList[position]
        holder.content.text = data.content
        holder.itemView.setOnClickListener {
            listener.onClick(position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: TextView

        init {
            content = itemView.findViewById(R.id.content)
        }

    }

}
