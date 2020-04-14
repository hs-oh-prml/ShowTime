package com.showtime.timetable


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.data.Schedule
import com.showtime.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_table.*
/**
 * A simple [Fragment] subclass.
 */
class TableFragment(var c: Context, var semesterNum:Int) : Fragment() {

    lateinit var semester: MyData.Semester

    lateinit var color: Array<String>
    lateinit var weekList:List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var pref =  PreferenceManager(c)
        semester = pref.myData.semester[pref.table]
        color = this.resources.getStringArray(R.array.colorList6)
        when(semester.dayMode){
            5->weekList = listOf("월", "화", "수", "목", "금")
            6->weekList = listOf("월", "화", "수", "목", "금","토")
            7->weekList = listOf("월", "화", "수", "목", "금","토","일")
            else->weekList = listOf("월", "화", "수", "목", "금")
        }
        initView(weekList)
        refreshTable()

    }

    fun refreshTable(){
        for(i in 1..22){
            for(j in 1..weekList.size){
//                var cell = row.getChildAt(j)
                var cell = getChild(i, j)
                cell.setBackgroundResource(R.color.white)
            }
        }

        for((index, i) in  semester.schedules.withIndex()){
            addTable(i, index)
        }
    }
    fun getChild(row:Int, col:Int): TextView {
        var index = (timeTable.columnCount * row) + col
        return timeTable.getChildAt(index) as TextView
    }

    fun addTable(schedule:Schedule, index:Int){
        var time = schedule.time
        for(i in time){
            var flag = 0
            for(j in i.start..i.end){

                var cell = getChild(j, i.week)

                if(flag == 0){
                    val name = schedule.name
                    val str = name
                    cell.textSize = 10f
                    cell.text = str
                    cell.gravity = Gravity.LEFT
                    cell.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                    cell.setPadding(5)
                    val shape:GradientDrawable = GradientDrawable()
                    shape.setColor(Color.parseColor(color[index]))
                    shape.shape = GradientDrawable.RECTANGLE
                    shape.cornerRadius = 15.0f
                    cell.background = shape

                    var param = GridLayout.LayoutParams()
                    var rowSpan = GridLayout.spec(j, (i.end - i.start + 1), GridLayout.FILL)
                    var colSpan = GridLayout.spec(i.week, 1, GridLayout.FILL)

                    param.rowSpec = rowSpan
                    param.columnSpec = colSpan
                    cell.layoutParams = param
                } else {
                    cell.visibility = View.GONE
                }
                flag++
            }

        }
    }

    override fun onResume() {
        super.onResume()
        refreshTable()
    }

    fun initView(weekList: List<String>){
        timeTable.columnCount = weekList.size + 1
        timeTable.rowCount = 23
        for(i in 0 until timeTable.rowCount){
            for(j in 0 until timeTable.columnCount){
                var param = GridLayout.LayoutParams()
                param.height = GridLayout.LayoutParams.WRAP_CONTENT
                param.width = GridLayout.LayoutParams.WRAP_CONTENT
                param.setMargins(1)
                param.setGravity(Gravity.CENTER)
                param.columnSpec = GridLayout.spec(j)
                param.rowSpec = GridLayout.spec(i)

                var textView = TextView(context)

                var disp = DisplayMetrics()
                var dwidth = disp.widthPixels
                var dheight = disp.heightPixels
                textView.gravity = Gravity.CENTER
                textView.setBackgroundResource(R.color.white)

                var colSpan = GridLayout.spec(j, GridLayout.FILL)
                var rowSpan = GridLayout.spec(i, GridLayout.FILL)
                param.columnSpec = colSpan
                param.rowSpec = rowSpan

                if(i == 0 && j != 0){
                    var colSpan = GridLayout.spec(j, GridLayout.FILL, 1f)
                    param.columnSpec = colSpan
                    textView.textSize = 10f
                    textView.text = weekList[j - 1]
                }
                if(i != 0 && j == 0){
                    if(i % 2 == 1){
                        textView.gravity = Gravity.TOP or Gravity.RIGHT
                        textView.textSize = 10f
                        if((9 + i / 2) > 12){
                            textView.text = ((9 + i / 2) % 12).toString()
                        } else {
                            textView.text = (9 + i / 2).toString()
                        }
                    }
                }
                if(j != 0){
                    var colSpan = GridLayout.spec(j, GridLayout.FILL, 1f)
                    param.columnSpec = colSpan
                }
                if(i != 0){
                    var rowSpan = GridLayout.spec(i, GridLayout.FILL, 1f)
//                    var rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2, 1f)
                    if(i % 2 == 1){
                        param.setMargins(1,1,1,0)
                    } else {
                        param.setMargins(1,0,1,1)
                    }
                    param.rowSpec = rowSpan
                }
                if(i != 0 && j != 0){
                    textView.width = (dwidth * (1/5) * 0.7).toInt()
                    textView.height = (dheight * (1/24) * 0.8).toInt()
                }
                textView.layoutParams = param
                textView.setTextColor(ContextCompat.getColor(context!!, R.color.table_text_color))
                timeTable.addView(textView)
            }
        }
    }
}

