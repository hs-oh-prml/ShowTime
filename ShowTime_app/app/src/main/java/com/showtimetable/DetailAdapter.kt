package com.showtimetable

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.*
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.data.Schedule
import com.showtimetable.sharedpreference.PreferenceManager

class DetailAdapter(
    val context: Context,
    var items:ArrayList<Schedule>,
    var tableNum:Int,
    var listener: DetailListener
): RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    var old_x=0f
    var new_x=0f

    interface DetailListener{
        fun refresh()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.detail_item, parent, false)
        return DetailViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val data = items[position]
        holder.name.text = data.name
        if(data.isLecture){
            holder.credit.text = "${data.credit}학점"
        }
        holder.delete.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setMessage("과목을 삭제하시겠습니까?").setTitle(holder.name.text.toString())
            builder.setPositiveButton("예"){
                    _,_->
                var pref = PreferenceManager(context)
                pref.myData.semester[tableNum].schedules.removeAt(position)
                pref.savePref()
                listener.refresh()

                val str = holder.name.text.toString() + "이(가) 삭제되었습니다."
                CustomToast(context, str).show()
            }
            builder.setNegativeButton("아니오"){
                    _,_->
            }

            val dlg = builder.create()
            dlg.show()

        }
    }



    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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