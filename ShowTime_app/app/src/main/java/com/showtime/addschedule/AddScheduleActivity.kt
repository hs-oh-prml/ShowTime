package com.showtime.addschedule

import android.content.Context
import android.content.Intent.getIntent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaCodec.MetricsConstants.MODE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.data.Schedule
import com.showtime.data.TimeCell
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.timetable.TableFragment
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.activity_add_schedule.timeTable
import kotlinx.android.synthetic.main.fragment_table.*

class AddScheduleActivity : AppCompatActivity(){

    lateinit var time:Array<Array<Int>>
    lateinit var semester: MyData.Semester

    lateinit var pref: PreferenceManager

    lateinit var color: Array<String>
    lateinit var weekList:List<String>

    var statusMap = Array(23){Array(6) { -1 } }
    var tableNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        init()

    }
    fun init(){
        pref = PreferenceManager(this)
        var intent = getIntent()
        tableNum = intent.getIntExtra("tableNum", 0)

        semester = pref.myData.semester[tableNum]
        when(semester.dayMode){
            5-> weekList = listOf("월", "화", "수", "목", "금")
            6-> weekList = listOf("월", "화", "수", "목", "금", "토")
            7-> weekList = listOf("월", "화", "수", "목", "금", "토", "일")
            else-> weekList = listOf("월", "화", "수", "목", "금")
        }
        var theme = pref.getTheme()
        color = this.resources.getStringArray(theme)
        weekList = listOf("월", "화", "수", "목", "금")
        initView(weekList)
        refreshTable()

//        radioGroup.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener{
//            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
////                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
////                Log.v("Radio Check", p1.toString())
//                timeTable.removeAllViews()
//                weekList = listOf("월", "화", "수", "목", "금")
//                initView(weekList)
////                when(p1){
////                    R.id.fiveDay->{
////                        weekList = listOf("월", "화", "수", "목", "금")
////                        initView(weekList)
////                    }
////                    R.id.sixDay->{
////                        weekList = listOf("월", "화", "수", "목", "금", "토")
////                        initView(weekList)
////                    }
////                    R.id.sevenDay-> {
////                        weekList = listOf("월", "화", "수", "목", "금", "토", "일")
////                        initView(weekList)
////                    }
////                }
//            }
//        })
        isLecture.setOnCheckedChangeListener { compoundButton, b ->
            if(isLecture.isChecked){
                credit.visibility = VISIBLE
                credit_text.visibility = VISIBLE
            } else {
                credit.visibility = GONE
                credit_text.visibility = GONE
            }
        }
        // 닫기 버튼 inae
        close_schedule.setOnClickListener {
            this.finish()
        }
        submit.setOnClickListener {

            //과목 이름 안썼을때
            if(name.text.isEmpty()){
                Toast.makeText(this,"과목 이름을 입력하세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //장소 체크
            var placeTxt = ""
            if(place.text.isNotEmpty()){
                // 장소 저장
                placeTxt = place.text.toString()
            }

            var isLecture = isLecture.isChecked
            var name = name.text.toString()
            var Ncredit:Int
            if(isLecture){
                if(credit.text.isEmpty())
                    Ncredit = 3
                else
                    Ncredit = credit.text.toString().toInt()
            } else{
                Ncredit = 0
            }

            var timeList = ArrayList<TimeCell>()
            for(i in 1..weekList.size){
                var timeCell = TimeCell(-1, -1, i, "")
                var startFlag = true
                for(j in 1..22){
                    if(statusMap[j][i] == 0){
                        if(startFlag){
                            timeCell.start= j
                            timeCell.end = j
                            startFlag = false
                        } else {
                            timeCell.end = j
                        }
                    } else {
                        if(!startFlag){
                            timeList.add(timeCell)
                            timeCell = TimeCell(-1, -1, i, "")
                            startFlag = true
                        }
                    }
                }

            }
            pref.myData.semester[tableNum].schedules.add(Schedule(isLecture, name, placeTxt, timeList, Ncredit, "A+"))
            pref.savePref()
            finish()
        }

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
                cell_name.setPadding(3, 3, 0, 0)
                cell_place.setPadding(3, 0, 0, 0)
                statusMap[j][i.week] = -2   // -2 이미 시간표가 있는 경우

                if(flag == 0){
                    val name = schedule.name
                    var place = ""
                    if(schedule.place != null){
                        place = schedule.place
                    }
                    cell_name.text = name
                    cell_place.text = place

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

                } else {
                    cell.visibility = View.GONE
                }
                flag++
            }

        }
    }

    fun initView(weekList: List<String>){

        var disp = DisplayMetrics()
        var dwidth = disp.widthPixels
        var dheight = disp.heightPixels

        timeTable.columnCount = weekList.size + 1
        timeTable.rowCount = 23
        for(j in 0 until timeTable.columnCount){
            for(i in 0 until timeTable.rowCount){

                var params = GridLayout.LayoutParams()
                params.height = WRAP_CONTENT
                params.width = WRAP_CONTENT
                params.setMargins(1)
                var child: View
                var colSpan = GridLayout.spec(j, GridLayout.FILL)
                var rowSpan = GridLayout.spec(i, GridLayout.FILL)
                params.columnSpec = colSpan
                params.rowSpec = rowSpan

                if(i == 0 && j == 0){

                    child = TextView(this)
                    child.setTextColor(ContextCompat.getColor(this, R.color.white))
                    child.textSize = 9f
                    child.text = "T"

                } else if(i == 0 && j != 0){

                    var colSpan = GridLayout.spec(j, GridLayout.FILL, 1f)
                    params.columnSpec = colSpan
                    child = TextView(this)
                    child.gravity = Gravity.CENTER
                    child.textSize = 9f
                    child.setIncludeFontPadding(false)
                    child.text = weekList[j - 1]

                } else if(i != 0 && j == 0){

                    child = TextView(this)
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
                    var inflater = LayoutInflater.from(this)
                    child = inflater.inflate(R.layout.table_item, timeTable, false)
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

                if(i != 0 && j != 0){
                    params.width = (dwidth * (1/5) * 0.7).toInt()
                    params.height = (dheight * (1/24) * 0.8).toInt()
                    child.setOnClickListener {
                        if(statusMap[i][j] == -1){
                            it.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_item))
                            statusMap[i][j] = 0

                        } else if(statusMap[i][j] == -2){
                            Toast.makeText(this, "겹치는 시간표가 존재합니다.", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            it.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                            statusMap[i][j] = -1
                        }
                    }
                }
                timeTable.addView(child)
            }
        }
    }


//    fun addTable(schedule:Schedule, index:Int){
//        var time = schedule.time
//        for(i in time){
//            var flag = 0
//            for(j in i.start..i.end){
//
//                var cell = getChild(j, i.week)
//                statusMap[j][i.week] = -2   // -2 이미 시간표가 있는 경우
//
//                if(flag == 0){
//                    val name = schedule.name
//                    val str = name
//                    cell.textSize = 10f
//                    cell.text = str
//                    cell.gravity = Gravity.LEFT
//                    cell.setTextColor(ContextCompat.getColor(this, R.color.white))
//                    cell.setPadding(5)
//                    val shape:GradientDrawable = GradientDrawable()
//                    shape.setColor(Color.parseColor(color[index]))
//                    shape.shape = GradientDrawable.RECTANGLE
//                    shape.cornerRadius = 15.0f
//                    cell.background = shape
//
//                    var param = GridLayout.LayoutParams()
//                    var rowSpan = GridLayout.spec(j, (i.end - i.start + 1), GridLayout.FILL)
//                    var colSpan = GridLayout.spec(i.week, 1, GridLayout.FILL)
//
//                    param.rowSpec = rowSpan
//                    param.columnSpec = colSpan
//                    cell.layoutParams = param
//                } else {
//                    cell.visibility = View.GONE
//                }
//                flag++
//            }
//
//        }
//    }
//
//    fun initView(weekList: List<String>){
//
//        timeTable.columnCount = weekList.size + 1
//        timeTable.rowCount = 23
//        for(i in 0 until timeTable.rowCount){
//            for(j in 0 until timeTable.columnCount){
//                var param = GridLayout.LayoutParams()
//                param.height = GridLayout.LayoutParams.WRAP_CONTENT
//                param.width = GridLayout.LayoutParams.WRAP_CONTENT
//                param.setMargins(1)
//                param.setGravity(Gravity.CENTER)
//                param.columnSpec = GridLayout.spec(j)
//                param.rowSpec = GridLayout.spec(i)
//
//                var textView = TextView(this)
//
//
//                var disp = DisplayMetrics()
//                var dwidth = disp.widthPixels
//                var dheight = disp.heightPixels
//                textView.gravity = Gravity.CENTER
//                textView.setBackgroundResource(R.color.white)
//
//                var colSpan = GridLayout.spec(j, GridLayout.FILL)
//                var rowSpan = GridLayout.spec(i, GridLayout.FILL)
//                param.columnSpec = colSpan
//                param.rowSpec = rowSpan
//
//                if(i == 0 && j != 0){
//                    var colSpan = GridLayout.spec(j, GridLayout.FILL, 1f)
//                    param.columnSpec = colSpan
//                    textView.textSize = 10f
//                    textView.text = weekList[j - 1]
//                }
//                if(i != 0 && j == 0){
//                    if(i % 2 == 1){
//                        textView.gravity = Gravity.TOP or Gravity.RIGHT
//                        textView.textSize = 10f
//                        if((9 + i / 2) > 12){
//                            textView.text = ((9 + i / 2) % 12).toString()
//                        } else {
//                            textView.text = (9 + i / 2).toString()
//                        }
//                    }
//                }
//                if(j != 0){
//                    var colSpan = GridLayout.spec(j, GridLayout.FILL, 1f)
//                    param.columnSpec = colSpan
//                }
//                if(i != 0){
//                    var rowSpan = GridLayout.spec(i, GridLayout.FILL, 1f)
////                    var rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2, 1f)
//                    if(i % 2 == 1){
//                        param.setMargins(1,1,1,0)
//                    } else {
//                        param.setMargins(1,0,1,1)
//                    }
//                    param.rowSpec = rowSpan
//                }
//                if(i != 0 && j != 0){
//                    textView.width = (dwidth * (1/5) * 0.7).toInt()
//                    textView.height = (dheight * (1/24) * 0.8).toInt()
//                }
//                textView.layoutParams = param
//                textView.setTextColor(ContextCompat.getColor(this, R.color.table_text_color))
//
//                if(i != 0 && j != 0){
//                    textView.setOnClickListener {
//                        if(statusMap[i][j] == -1){
//                            it.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_item))
//                            statusMap[i][j] = 0
//
//                        } else if(statusMap[i][j] == -2){
//                            Toast.makeText(this, "겹치는 시간표가 존재합니다.", Toast.LENGTH_SHORT).show()
//                        }
//                        else {
//                            it.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
//                            statusMap[i][j] = -1
//                        }
//                    }
//                }
//                timeTable.addView(textView)
//            }
//        }
//    }
//
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }


}
