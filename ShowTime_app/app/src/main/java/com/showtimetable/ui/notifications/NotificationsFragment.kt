package com.showtimetable.ui.notifications

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import androidx.core.view.setPadding
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.showtimetable.GraphAxisValueFormatter
import com.showtimetable.R
import com.showtimetable.data.MyData
import com.showtimetable.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_notifications.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NotificationsFragment : Fragment() {

    //val weekList = arrayListOf<String>("월","화","수","목","금")
    lateinit var semester: Array<String>
    lateinit var scoreList: ArrayList<Float>

    lateinit var pref: PreferenceManager
    lateinit var myData:MyData

    var total_credit = 0
    lateinit var adapter:ViewPagerAdapter
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
        initData()
        init()
    }
    fun initData(){
        // Data init
        pref = PreferenceManager(requireContext())
        myData = pref.myData
        scoreList = ArrayList()

        //평점 통계
        setScore()

        //차트 생성
        setChart()

        taking_score.text = "${total_credit}학점"
        var gdp = 0.0f
        var score_num = 0
        for(i in scoreList){ // score 이 nan 이면 패스
            if(!(i.isNaN())){
                gdp += i
                score_num++
            }
        }
        gdp /= score_num.toFloat()
        total_score.text = "%.2f/4.5".format(gdp)
    }
    fun init(){
        // Init View Pager
        var listener = object: SemesterAdapter.SemesterAdapterListener{
            override fun spinnerChanged() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                initData()
            }
        }
        adapter = ViewPagerAdapter(requireContext(), myData.semester, listener)
        viewpager.adapter = adapter

        TabLayoutMediator(tabLayout, viewpager, object : TabLayoutMediator.TabConfigurationStrategy{
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                tab.setText(myData.semester[position].semester)

            }
        }).attach()

    }

    fun setScore(){
        // check
        total_credit = 0
        for(i in myData.semester){
            var score = 0.0f
            var credit = 0.0f

            for(j in i.schedules){
                if(j.isLecture){
                    total_credit += j.credit
                    when(j.score){
                        "A+"->{
                            score += (4.5f * j.credit)
                            credit += j.credit
                        }
                        "A"->{
                            score += (4.0f * j.credit)
                            credit += j.credit
                        }
                        "B+"->{
                            score += (3.5f * j.credit)
                            credit += j.credit
                        }
                        "B"->{
                            score += (3.0f * j.credit)
                            credit += j.credit
                        }
                        "C+"->{
                            score += (2.5f * j.credit)
                            credit += j.credit
                        }
                        "C"->{
                            score += (2.0f * j.credit)
                            credit += j.credit
                        }
                        "D+"->{
                            score += (1.5f * j.credit)
                            credit += j.credit
                        }
                        "D"->{
                            score += (1.0f * j.credit)
                            credit += j.credit
                        }
                        "F"->{
                            score += 0f
                            credit += j.credit
                        }
                        else->{

                        }
                    }
                }
            }
            scoreList.add((score/credit).toFloat())
        }
    }

    fun setChart(){
        val lineChart =lineChart
        lineChart.invalidate()
        lineChart.clear()
        val values = ArrayList<Entry>()
        //val x_value = ArrayList<>
        var index = 0
        val x_list = ArrayList<String>()
        for(i in 0 until scoreList.size){
            if(!scoreList[i].isNaN()){
                values.add(Entry(index.toFloat(),scoreList[i]))
                index++
                when(i){
                    0->x_list.add("1-1")
                    1->x_list.add("1-2")
                    2->x_list.add("2-1")
                    3->x_list.add("2-2")
                    4->x_list.add("3-1")
                    5->x_list.add("3-2")
                    6->x_list.add("4-1")
                    7->x_list.add("4-2")
                }
            }
        }


        val lineDataSet = LineDataSet(values,"학점")
        lineDataSet.setColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
        lineDataSet.setCircleColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
        lineDataSet.valueTextSize = 9f
//        lineDataSet.circleHoleColor = ContextCompat.getColor(this.context!!, R.color.colorPrimary)

        val lineData = LineData()
        lineData.addDataSet(lineDataSet)
        lineData.setValueTextColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))



        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        if(values.size > 1){
            xAxis.axisMinimum = 0f
            xAxis.mAxisMaximum = values.size.toFloat()-1
            xAxis.setLabelCount(values.size,true)
        }else{
            xAxis.setLabelCount(values.size,false)
        }

        xAxis.valueFormatter = GraphAxisValueFormatter(x_list)

        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.textColor = ContextCompat.getColor(this.context!!, R.color.middle_gray)


        val yAxisRight = lineChart.axisLeft
        yAxisRight.isEnabled = true
        yAxisRight.gridColor = ContextCompat.getColor(this.context!!, R.color.middle_gray)
        yAxisRight.textColor = ContextCompat.getColor(this.context!!, R.color.middle_gray)
        //yAxisRight.setCenterAxisLabels(true)
        yAxisRight.labelCount = 4
        yAxisRight.axisMinimum = 0.0f
        yAxisRight.axisMaximum = 4.5f

        //yAxisRight.gridLineWidth =
        yAxisRight.setDrawLabels(true)
        yAxisRight.setDrawAxisLine(false)
        yAxisRight.setDrawGridLines(true)

        yAxisRight.mAxisMaximum = 4.5f
        yAxisRight.mAxisMinimum = 0f
        val yAxisLeft = lineChart.axisRight
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
}
