package com.showtimetable.addschedule

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
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.showtimetable.CustomToast
import com.showtimetable.R
import com.showtimetable.data.MyData
import com.showtimetable.data.Schedule
import com.showtimetable.data.TimeCell
import com.showtimetable.sharedpreference.PreferenceManager
import com.showtimetable.timetable.TableFragment
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.activity_add_schedule.timeTable
import kotlinx.android.synthetic.main.fragment_table.*

class AddScheduleActivity : AppCompatActivity(){

    lateinit var time:Array<Array<Int>>
    lateinit var semester: MyData.Semester

    lateinit var pref: PreferenceManager

    lateinit var color: Array<String>
    lateinit var weekList:List<String>
    var dwidth=0
    var dheight=0

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
        setSemesterTitle(tableNum)

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
                val str = "과목 이름을 입력하세요"
                CustomToast(this,str).show()
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

            val toast = CustomToast(this, "["+name+"] 과목이 추가되었습니다.")
            toast.show()
            finish()
        }

    }

    private fun setSemesterTitle(tableNum: Int) {
        semester_title.text = when(tableNum){
            0->"1학년  1학기"
            1->"1학년  2학기"
            2->"2학년  1학기"
            3->"2학년  2학기"
            4->"3학년  1학기"
            5->"3학년  2학기"
            6->"4학년  1학기"
            7->"4학년  2학기"
            else->null
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
                cell_name.setPadding(3, 3, 3, 0)
                cell_place.setPadding(3, 0, 3, 0)
                statusMap[j][i.week] = -2   // -2 이미 시간표가 있는 경우

                if(flag == 0){
                    val name = schedule.name
                    var place = ""
                    if(schedule.place != null){
                        place = schedule.place
                    }
                    cell_name.text = name
                    cell_place.text = place
                    cell_place.width = (dwidth / 21) * 4 - 10

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
        //inae
        var table_params = LinearLayout.LayoutParams(
            WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        table_params.setMargins(30)
        table_frame2.layoutParams = table_params

        //table_params.
        //table_frame.layoutParams = table_params
        //table_frame.setPadding(20)
        //hi.setPadding(10)

        timeTable.columnCount = weekList.size + 1
        timeTable.rowCount = 23

        var disp = DisplayMetrics()
        this.windowManager?.defaultDisplay?.getMetrics(disp)
        dwidth = disp.widthPixels - 60
        dheight = disp.heightPixels

        for(j in 0 until timeTable.columnCount){
            for(i in 0 until timeTable.rowCount){

                var params = GridLayout.LayoutParams()
                //params.setMargins(1)
                var child: View
                var colSpan = GridLayout.spec(j, GridLayout.FILL)
                var rowSpan = GridLayout.spec(i, GridLayout.FILL)
                params.columnSpec = colSpan
                params.rowSpec = rowSpan

                if(i == 0 && j == 0){

                    child = TextView(this)
                    child.setTextColor(ContextCompat.getColor(this, R.color.white))
                    child.textSize = 10f

                } else if(i == 0 && j != 0){ // 월화수목금


                    var colSpan = GridLayout.spec(j, GridLayout.FILL)
                    params.columnSpec = colSpan
                    child = TextView(this)
                    child.gravity = Gravity.CENTER
                    child.textSize = 9f
                    child.text = weekList[j - 1]
                } else if(i != 0 && j == 0){ //시간 9~7

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
                    params.width = dwidth / 21 //크기 21중 1만큼 차지
                } else { // 가운데 칸들
                    var inflater = LayoutInflater.from(this)
                    child = inflater.inflate(R.layout.table_item, timeTable, false)
//                    params.width = (dwidth * (1/5) * 0.7).toInt()
//                    params.height = (dheight * (1/24) * 0.8).toInt()
                    params.width = (dwidth / 21) * 4
                    params.height = 50
                }
                if(i != 0){
                    var rowSpan = GridLayout.spec(i, GridLayout.FILL,1f)
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
                    child.setOnClickListener {
                        if(statusMap[i][j] == -1){
                            it.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_item))
                            statusMap[i][j] = 0

                        } else if(statusMap[i][j] == -2){
                            val str = "겹치는 시간표가 존재합니다."
                            CustomToast(this,str).show()
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
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }


}
