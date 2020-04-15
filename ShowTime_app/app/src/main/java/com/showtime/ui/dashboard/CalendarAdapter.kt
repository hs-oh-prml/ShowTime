package com.showtime.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.w3c.dom.Text
import java.util.*

class CalendarAdapter(
    var context: Context,
    var today: Calendar,
    var listener:calendarListener
): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    interface calendarListener{
        fun onClick(month:Int, year:Int, clickDate:Int, weekDay:String,  v:View)
    }

    var pref: PreferenceManager = PreferenceManager(context)

    var clickDate = -1

    val CURRENT = Int.MAX_VALUE / 2

//    var week = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    var week = listOf("일", "월", "화", "수", "목", "금", "토")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_layout, parent, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return Integer.MAX_VALUE
    }
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.setIsRecyclable(false)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.setIsRecyclable(false)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        var gap = position - CURRENT
        var cal = today.clone() as Calendar
        cal.add(Calendar.MONTH, gap)
        cal.set(Calendar.DATE, 1)

        var month = cal.get(Calendar.MONTH) + 1
        var year = cal.get(Calendar.YEAR)
        holder.title.text = "${year}년 ${month}월"

        var count = 1
//        holder.calendar.layoutParams = grid_params
        for(i in 0..6){
            for(j in 0..6){
                if(i == 0){
                    var textView = TextView(context)
                    textView.gravity = Gravity.CENTER
//                    textView.setBackgroundResource(R.color.white)
                    textView.setBackgroundResource(R.drawable.table_border)
                    textView.text = week[j]
                    textView.textSize = 10f
                    if(j == 0){
                        textView.setTextColor(ContextCompat.getColor(context, R.color.red))
                    } else if(j == 6){
                        textView.setTextColor(ContextCompat.getColor(context, R.color.blue))
                    }  else {
                        textView.setTextColor(ContextCompat.getColor(context, R.color.black))
                    }
                    var params = GridLayout.LayoutParams()
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)

                    textView.layoutParams = params

                    holder.calendar.addView(textView)
                } else {

                    var inflater = LayoutInflater.from(context)
//                var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    var child = inflater.inflate(R.layout.calendar_item, holder.calendar, false)

//                var params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1f)
//                child.layoutParams = params
                    var params = GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                    )
                    params.height = 0
                    params.width = 0
                    child.layoutParams = params
                    var isToday = child.findViewById<ImageView>(R.id.isToday)
                    var isScheduled = child.findViewById<View>(R.id.isScheduled)
                    var date = child.findViewById<TextView>(R.id.date)
                    date.textSize = 10f
                    if(count <= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        if (i == 1) {
                            if ((cal.get(Calendar.DAY_OF_WEEK) - 1) <= j) {
                                date.text = "${count}"
                                count++
                            }
                        } else {
                            date.text = "${count}"
                            count++
                        }
                    }
                    if(year == today.get(Calendar.YEAR) && month == (today.get(Calendar.MONTH) + 1) && count-1 == today.get(Calendar.DATE)){
//                        val shape: GradientDrawable = GradientDrawable()
//                        shape.setColor(ContextCompat.getColor(context, R.color.white))
//                        shape.shape = GradientDrawable.RECTANGLE
//                        shape.cornerRadius = 15.0f
//                        shape.setStroke(1,ContextCompat.getColor(context, R.color.blue))
//                        schedule_item_layout.background = shape
                        //schedule_item_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                        isToday.visibility = VISIBLE
                    }
                    if(j == 0){
                        date.setTextColor((ContextCompat.getColor(context, R.color.red)))
                    }else if(j == 6){
                        date.setTextColor((ContextCompat.getColor(context, R.color.blue)))
                    } else {
                        date.setTextColor((ContextCompat.getColor(context, R.color.black)))
                    }
                    var str = pref.getDaySchedule(year, month, count-1)
                    if(str != ""){
                        isScheduled.visibility = VISIBLE
                    }
                    //child.isScheduled.visibility = VISIBLE
                    holder.calendar.addView(child)
                }

                for((index, i) in holder.calendar.children.withIndex()){
                    if(index < 7){
                        continue
                    }
                    i.setOnClickListener {
                        var i_date = i.findViewById<TextView>(R.id.date)
                        if(i_date.text.toString() != ""){
                            if(clickDate == index){
                                i.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                                if(index % 7 == 0){
                                    i_date.setTextColor((ContextCompat.getColor(context, R.color.red)))
                                }else if(index % 7 == 6){
                                    i_date.setTextColor((ContextCompat.getColor(context, R.color.blue)))
                                } else {
                                    i_date.setTextColor((ContextCompat.getColor(context, R.color.black)))
                                }
                                clickDate = -1
                            } else {
                                if(clickDate != -1){
                                    var prev = holder.calendar.getChildAt(clickDate)
                                    var prev_date = prev.findViewById<TextView>(R.id.date)
                                    prev.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                                    if(clickDate % 7 == 0){
                                        prev_date.setTextColor((ContextCompat.getColor(context, R.color.red)))
                                    }else if(clickDate % 7 == 6){
                                        prev_date.setTextColor((ContextCompat.getColor(context, R.color.blue)))
                                    } else {
                                        prev_date.setTextColor((ContextCompat.getColor(context, R.color.black)))
                                    }
                                }
                                //i.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                                val shape: GradientDrawable = GradientDrawable()
                                shape.setColor(ContextCompat.getColor(context, R.color.white))
                                shape.shape = GradientDrawable.RECTANGLE
                                shape.cornerRadius = 15.0f
                                shape.setStroke(2,ContextCompat.getColor(context, R.color.colorPrimary))
                                i.background = shape
                                i_date.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                                clickDate = index
                            }
                            listener.onClick(month, year, clickDate, week[index % 7], it)
                        }
                    }
                }

            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var title: TextView
        var calendar: GridLayout
//        var calendar_item:LinearLayout
        init{
            title = itemView.findViewById(R.id.title)
            calendar = itemView.findViewById(R.id.calendar_grid)
//            calendar_item = itemView.findViewById(R.id.calendar_item_layout)
        }
    }

}