package com.showtime.ui.notifications

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.showtime.R
import kotlinx.android.synthetic.main.fragment_notifications.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NotificationsFragment : Fragment() {

    //val weekList = arrayListOf<String>("월","화","수","목","금")
    lateinit var semester: Array<String>
    var score = arrayListOf<Float>(2f,4f,2f,4f,3f,4f,3f,4.5f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        semester = this.resources.getStringArray(R.array.semester)

        init()



    }

    fun init(){

        //setPieChart()
        //setLineChart()
        //차트 생성
        setChart()
        //평점 통계
        setScore()

    }

    fun setScore(){

    }

    fun setChart(){
        val lineChart =lineChart
        lineChart.invalidate()
        lineChart.clear()

        val values = ArrayList<Entry>()
        for(i in 0..7){
            values.add(Entry(i.toFloat(),score[i]))
        }

        val lineDataSet = LineDataSet(values,"학점")
        lineDataSet.setColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
        lineDataSet.setCircleColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
        lineDataSet.circleHoleColor = ContextCompat.getColor(this.context!!, R.color.colorPrimary)

        val lineData = LineData()
        lineData.addDataSet(lineDataSet)
        lineData.setValueTextColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))



        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setLabelCount(8,true)
//        xAxis.textSize =
        xAxis.valueFormatter = object: ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return when(value){
                    0f->"1 - 1"
                    1f->"1 - 2"
                    2f->"2 - 1"
                    3f->"2 - 2"
                    4f->"3 - 1"
                    5f->"3 - 2"
                    6f->"4 - 1"
                    7f->"4 - 2"
                    else->"null"
                }
            }
        }
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)


        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = true
        yAxisRight.gridColor = ContextCompat.getColor(this.context!!, R.color.table_text_color)
        yAxisRight.textColor = ContextCompat.getColor(this.context!!, R.color.table_text_color)
        yAxisRight.setDrawLabels(true)
        yAxisRight.setDrawAxisLine(false)
        yAxisRight.setDrawGridLines(true)
//        yAxisRight.gridLineWidth = 0.5f
//        yAxisRight.spaceMax = 0.5f
//        yAxisRight.axisMinimum = 0f
//        yAxisRight.axisMaximum = 4.5f

        yAxisRight.mAxisMaximum = 4.5f
        yAxisRight.mAxisMinimum = 0f
//        yAxisLeft.gridLineWidth = 0.5f
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.isEnabled = false

        val legend = lineChart.legend
        legend.isEnabled = false

        lineChart.description = null
        lineChart.data = lineData
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawGridBackground(false)
        lineChart.setHighlightPerDragEnabled(false)
        lineChart.setTouchEnabled(false)
    }

//    fun setLineChart(){
//        val xAxis = lineChart.xAxis
//
//        xAxis.apply {
//            position = XAxis.XAxisPosition.BOTTOM
//            textSize = 10f
//            setDrawGridLines(false)
//            granularity = 1f
//            axisMinimum = 2f
//            //isGranularityEnabled = true
//            setLabelCount(8,true)
//        }
//
//        lineChart.apply {
//            axisRight.isEnabled = false
//            axisLeft.axisMaximum = 4.5f
//            axisLeft.axisMinimum = 0f
//            legend.apply {
//                textSize = 15f
//                verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
//                orientation = Legend.LegendOrientation.HORIZONTAL
//                setDrawInside(false)
//            }
//        }
//
//        val lineData = LineData()
//        lineChart.data = lineData
//        addEntry()
//
//    }
//
//    fun addEntry(){
//        val data = lineChart.data
//        data?.let {
//            var set:ILineDataSet?=data.getDataSetByIndex(0)
//            if(set == null) {
//                set = createSet()
//                data.addDataSet(set)
//            }
//
//            data.addEntry(Entry(set.entryCount.toFloat(),3.0f),0)
//            lineChart.apply {
//                notifyDataSetChanged()
//                //moveViewToX(1f)
//                setVisibleXRangeMaximum(4f)
//                setPinchZoom(false)
//                isDoubleTapToZoomEnabled = false
//                description.text = "학기"
////                setBackgroundColor()
//                description.textSize = 15f
//                setExtraOffsets(8f,16f,8f,16f)
//            }
//
//        }
//
//    }
//
//    fun createSet():LineDataSet {
//        val values = ArrayList<Entry>()
//        for(i in 0..7){
//            values.add(Entry(score[i],i.toFloat()))
//        }
//        val set = LineDataSet(values,"전체 평점")
//        set.apply {
//            axisDependency = YAxis.AxisDependency.LEFT
//            color= ContextCompat.getColor(context!!, R.color.colorPrimary)
//            setCircleColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
//            valueTextSize = 10f
//            lineWidth = 2f
//            circleRadius = 3f
//            fillAlpha = 0
//            fillColor = resources.getColor((R.color.colorPrimary))
//            highLightColor = R.color.red
//            setDrawValues(true)
//        }
//        return set
//    }

//    fun setPieChart(){
//        println("make chart1")
//        total_score_chart.clearChart()
//        total_score_chart.addPieSlice(PieModel("type1", 3.5F,R.color.red))
//        //total_score_chart.addPieSlice(PieModel("type2",,R.color.white))
//        total_score_chart.startAnimation()
//    }

//    fun initView(weekList: List<String>){
//        var rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
//        var colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
//        var gridParam = GridLayout.LayoutParams(rowSpan, colSpan)
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
//                var textView = TextView(context)
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
//                textView.setTextColor(ContextCompat.getColor(context!!, R.color.table_text_color))
//                timeTable.addView(textView)
//            }
//        }
//    }
}

