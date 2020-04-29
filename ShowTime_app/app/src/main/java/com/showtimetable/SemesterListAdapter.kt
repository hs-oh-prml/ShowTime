package com.showtimetable

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.data.MyData
import com.showtimetable.sharedpreference.PreferenceManager

class SemesterListAdapter (
    val context: Context,
    var items:ArrayList<MyData.Semester>,
    var fm: FragmentManager,
    var listener: SemesterListener
): RecyclerView.Adapter<SemesterListAdapter.ViewHolder>()
{

    interface SemesterListener{
        fun refresh()
    }

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
        holder.semester.textSize = 12f

        var pref = PreferenceManager(context)

        if(pref.table == position){
            holder.is_selected.visibility = VISIBLE
        } else {
            holder.is_selected.visibility = INVISIBLE
        }

        //inae
        holder.is_selected_btn.setOnClickListener {
            pref.setTableNum(position)
            listener.refresh()
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

        holder.credit.textSize = 12f
        holder.lecture_count.textSize = 12f

        holder.item_semester.setOnClickListener {
//            var intent = Intent(context, DetailDialog::class.java)
//            intent.putExtra("tableNum", position)
//            context.startActivity(intent)
            var dListener = object: DetailDialog.detailDialogDismissListener {
                override fun onDismiss() {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    Log.d("Dialog Status", "Dismiss")
                    listener.refresh()
                }
            }
            var dialog = DetailDialog(context, position, dListener)
            fm.executePendingTransactions()
            dialog.show(fm, "")

//            {
//                Log.d("Dialog", "Dismiss")
//            }
            fm.executePendingTransactions()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var semester: TextView
        var is_selected:ImageView
        var credit:TextView
        var lecture_count:TextView
        var item_semester:LinearLayout
        var is_selected_btn:LinearLayout
        init {
            semester = itemView.findViewById(R.id.semester)
            is_selected = itemView.findViewById(R.id.is_selected)
            credit = itemView.findViewById(R.id.credit)
            lecture_count = itemView.findViewById(R.id.lecture_count)
            item_semester = itemView.findViewById(R.id.item_semester)
            is_selected_btn = itemView.findViewById(R.id.is_selected_btn)
        }

    }

}