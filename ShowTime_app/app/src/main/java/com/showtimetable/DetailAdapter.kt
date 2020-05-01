package com.showtimetable

import android.app.Activity
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
            var pref = PreferenceManager(context)
            pref.myData.semester[tableNum].schedules.removeAt(position)
            pref.savePref()
            listener.refresh()

            val str = holder.name.text.toString() + "이(가) 삭제되었습니다."
            CustomToast(context, str).show()
        }
//        holder.name.setOnTouchListener(object : View.OnTouchListener{
//            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
//
//                if(event?.action == MotionEvent.ACTION_DOWN){
//                    println("down : "+event.x)
//                    old_x = event.x
//                }else if(event?.action == MotionEvent.ACTION_MOVE){
//                    new_x = event.x
//                    println("up : "+event.x)
//                    if(old_x > new_x){
//                        holder.delete.visibility = VISIBLE
//                        new_x = 0f
//                        old_x = 0f
//                    }
//                }
//
//                return true
//            }
//
//        })
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