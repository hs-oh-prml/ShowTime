package com.showtimetable

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.data.Schedule
import com.showtimetable.sharedpreference.PreferenceManager

class DetailAdapter(
    val context: Context,
    var items:ArrayList<Schedule>,
    var tableNum:Int,
    var listener: DetailListener
): RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    interface DetailListener{
        fun refresh()
    }
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
        holder.delete.setOnClickListener {
            var pref = PreferenceManager(context)
            pref.myData.semester[tableNum].schedules.removeAt(position)
            pref.savePref()
            listener.refresh()

            val str = holder.name.text.toString() + "이(가) 삭제되었습니다."
            CustomToast(context, str).show()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var credit:TextView
        var delete: TextView
        init {
            name = itemView.findViewById(R.id.name)
            credit = itemView.findViewById(R.id.credit)
            delete = itemView.findViewById(R.id.delete)
        }

    }

}