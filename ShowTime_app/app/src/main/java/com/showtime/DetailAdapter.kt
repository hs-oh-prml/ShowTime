package com.showtime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.showtime.data.Schedule
import com.showtime.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.custom_toast.view.*

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


            var inflater = LayoutInflater.from(context)
            var t_view = inflater.inflate(R.layout.custom_toast,null, false)
            //var custom_toast = LayoutInflater.from(context).inflate(R.layout.toast_design, null)
            t_view.toast_text.text = holder.name.text.toString() + "이(가) 삭제되었습니다."
            var t2 = Toast(this.context)
            t2.view = t_view
            t2.show()

//            val str = holder.name.text.toString() + "이(가) 삭제되었습니다."
//            Toast.makeText(context!!,str,Toast.LENGTH_SHORT ).show()
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