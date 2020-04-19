package com.showtime.timetable


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.data.Schedule
import com.showtime.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.fragment_table.*
import kotlinx.android.synthetic.main.fragment_table.timeTable
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 */
class TableFragment(var c: Context, var semesterNum:Int) : Fragment() {

    lateinit var semester: MyData.Semester
    lateinit var color: Array<String>
    lateinit var weekList:List<String>
    lateinit var pref:PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pref =  PreferenceManager(c)
        semester = pref.myData.semester[pref.table]
        var theme = pref.getTheme()
        color = this.resources.getStringArray(theme)
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
                var cell = getChild(i, j)
                cell.setBackgroundResource(R.color.white)
            }
        }

        for((index, i) in  semester.schedules.withIndex()){
            addTable(i, index)
        }
    }
    fun getChild(row:Int, col:Int): View {
        var index = (timeTable.columnCount * row) + col
        return timeTable.getChildAt(index)
    }

    fun addTable(schedule:Schedule, index:Int){
        var time = schedule.time
        for(i in time){
            var flag = 0
            for(j in i.start..i.end){

                var cell = timeTable.getChildAt((timeTable.rowCount * i.week) + j)
                var cell_name = cell.findViewById<TextView>(R.id.cell_name)
                var cell_place =  cell.findViewById<TextView>(R.id.cell_place)

                if(flag == 0){
                    val name = schedule.name
                    var place = ""
                    if(schedule.place != null){
                        place = schedule.place
                    }
                    cell_name.text = name
                    cell_place.text = place
                    cell_name.setPadding(3, 3, 0, 0)
                    cell_place.setPadding(3, 0, 0, 0)

                    val shape = GradientDrawable()
                    shape.setColor(Color.parseColor(color[index % color.size]))
                    shape.shape = GradientDrawable.RECTANGLE
                    shape.cornerRadius = 15.0f
                    cell.background = shape

                    var param = GridLayout.LayoutParams()
                    var rowSpan = GridLayout.spec(j, (i.end - i.start + 1), GridLayout.FILL)
                    var colSpan = GridLayout.spec(i.week, 1, GridLayout.FILL)
                    param.rowSpec = rowSpan
                    param.columnSpec = colSpan
                    cell.layoutParams = param

                    cell.setOnClickListener {
                        var dialog = CellDialog(context!!,semesterNum, index)
                        var fm = parentFragmentManager


                        dialog.show(fm, "")

                    }
                } else {
                    cell.visibility = View.GONE
                }
                flag++
            }

        }
    }

    fun initView(weekList: List<String>){
        timeTable.columnCount = weekList.size + 1
        timeTable.rowCount = 23

        var disp = DisplayMetrics()
        var dwidth = disp.widthPixels
        var dheight = disp.heightPixels

        for(j in 0 until timeTable.columnCount){
            for(i in 0 until timeTable.rowCount){

                var params = GridLayout.LayoutParams()
                params.setMargins(1)
                var child: View
                var colSpan = GridLayout.spec(j, GridLayout.FILL)
                var rowSpan = GridLayout.spec(i, GridLayout.FILL)
                params.columnSpec = colSpan
                params.rowSpec = rowSpan

                if(i == 0 && j == 0){

                    child = TextView(context)
                    child.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                    child.textSize = 10f

                } else if(i == 0 && j != 0){

                    var colSpan = GridLayout.spec(j, GridLayout.FILL, 1f)
                    params.columnSpec = colSpan
                    child = TextView(context)
                    child.gravity = Gravity.CENTER
                    child.textSize = 9f
                    child.text = weekList[j - 1]

                } else if(i != 0 && j == 0){

                    child = TextView(context)
                    child.gravity = Gravity.TOP or Gravity.RIGHT
                    child.textSize = 9f
                    if(i % 2 == 1){
                        if((9 + i / 2) > 12){
                            child .text = ((9 + i / 2) % 12).toString()
                        } else {
                            child .text = (9 + i / 2).toString()
                        }
                    }
                } else {
                    var inflater = LayoutInflater.from(context)
                    child = inflater.inflate(R.layout.table_item, timeTable, false)
                    params.width = (dwidth * (1/5) * 0.7).toInt()
                    params.height = (dheight * (1/24) * 0.8).toInt()
                }
                if(i != 0){
                    var rowSpan = GridLayout.spec(i, GridLayout.FILL, 1f)
                    params.rowSpec = rowSpan
                }
                if(i % 2 == 1){
                    params.setMargins(1,1,1,0)
                } else {
                    params.setMargins(1,0,1,1)
                }
                child.setBackgroundResource(R.color.white)
                child.layoutParams = params
                timeTable.addView(child)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pref =  PreferenceManager(c)
        var theme = pref.getTheme()
        color = this.resources.getStringArray(theme)
        refreshTable()
    }
}

