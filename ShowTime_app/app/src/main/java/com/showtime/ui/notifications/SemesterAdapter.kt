package com.showtime.ui.notifications

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.Schedule
import com.showtime.sharedpreference.PreferenceManager

class SemesterAdapter  (
    val context: Context,
    var semesterNum:Int,
    var listener:SemesterAdapterListener
): RecyclerView.Adapter<SemesterAdapter.ViewHolder>() {

    var pref = PreferenceManager(context)


    interface SemesterAdapterListener{
        fun spinnerChanged()
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.setIsRecyclable(false)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.setIsRecyclable(true)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.semester_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return pref.myData.semester[semesterNum].schedules.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = pref.myData.semester[semesterNum].schedules[position]

//        Log.d("RECYCLER VIEW", data.name)
        holder.name.text = data.name
        var adapter = ArrayAdapter(context, R.layout.spinner_layout, context.resources.getStringArray(R.array.score))

        if(data.isLecture){
            holder.score.visibility = VISIBLE
        } else {
            holder.score.visibility = GONE
        }
        holder.credit.text = "${data.credit} 학점"
        holder.score.adapter = adapter
        holder.score.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                pref = PreferenceManager(context)
                pref.myData.semester[semesterNum].schedules[position].score = holder.score.selectedItem.toString()
                pref.savePref()
                listener.spinnerChanged()
            }
        }
        if(data.isLecture){
            when(data.score){
                "A+"->{
                    holder.score.setSelection(0)
                }
                "A"->{
                    holder.score.setSelection(1)
                }
                "B+"->{
                    holder.score.setSelection(2)
                }
                "B"->{
                    holder.score.setSelection(3)
                }
                "C+"->{
                    holder.score.setSelection(4)
                }
                "C"->{
                    holder.score.setSelection(5)
                }
                "D+"->{
                    holder.score.setSelection(6)
                }
                "D"->{
                    holder.score.setSelection(7)
                }
                "F"->{
                    holder.score.setSelection(8)
                }
                "P"->{
                    holder.score.setSelection(9)
                }
                "NP"->{
                    holder.score.setSelection(10)
                }
                else->{
                    holder.score.setSelection(0)

                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var score: Spinner
        var credit:TextView
        init {
            name = itemView.findViewById(R.id.name)
            score = itemView.findViewById(R.id.score)
            credit = itemView.findViewById(R.id.credit)
        }

    }

}