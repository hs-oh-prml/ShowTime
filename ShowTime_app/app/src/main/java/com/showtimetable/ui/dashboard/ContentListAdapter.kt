package com.showtimetable.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.CustomToast
import com.showtimetable.R
import com.showtimetable.data.CalendarData
import com.showtimetable.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_table.*

class ContentListAdapter(
    var context: Context,
    var itemList:ArrayList<CalendarData.CalendarItem>,
    var listener:Listener,
    var y:Int,
    var m:Int,
    var d:Int
): RecyclerView.Adapter<ContentListAdapter.ViewHolder>() {

    interface Listener{
        fun onClick(pos:Int)
        fun refresh()
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
        holder.delete.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setMessage("일정을 삭제하시겠습니까?").setTitle("일정 삭제")
            builder.setPositiveButton("예"){
                    _,_->
                var pref = PreferenceManager(context)
                pref.deleteDaySchedule(y, m, d, position)
                val str = "${m}월 ${d}일 일정이 삭제되었습니다."
                CustomToast(context, str).show()
                listener.refresh()
            }
            builder.setNegativeButton("아니오"){
                    _,_->
            }

            val dlg = builder.create()
            dlg.show()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: TextView
        var delete: TextView

        init {
            content = itemView.findViewById(R.id.content)
            delete = itemView.findViewById(R.id.delete)

        }

    }

}
