package com.showtime.ui.notifications

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
import com.showtime.R
import com.showtime.data.MyData
import com.showtime.sharedpreference.PreferenceManager
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
        Log.d("MY DATA", myData.semester.toString())
        scoreList = ArrayList()

        //평점 통계
        setScore()

        //차트 생성
        setChart()

        taking_score.text = "${total_credit}학점"
        var gdp = 0.0f
        for(i in scoreList){
            gdp += i
        }
        gdp /= scoreList.size

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
        var adapter = ViewPagerAdapter(requireContext(), myData.semester, listener)
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
            scoreList.add(score/credit)
        }
    }

    fun setChart(){
        val lineChart =lineChart
        lineChart.invalidate()
        lineChart.clear()

        val values = ArrayList<Entry>()
        for(i in 0 until scoreList.size){
            values.add(Entry(i.toFloat(),scoreList[i]))
        }

        val lineDataSet = LineDataSet(values,"학점")
        lineDataSet.setColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
        lineDataSet.setCircleColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
//        lineDataSet.circleHoleColor = ContextCompat.getColor(this.context!!, R.color.colorPrimary)

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
        xAxis.textColor = ContextCompat.getColor(this.context!!, R.color.table_text_color)


        val yAxisRight = lineChart.axisLeft
        yAxisRight.isEnabled = true
        yAxisRight.gridColor = ContextCompat.getColor(this.context!!, R.color.table_text_color)
        yAxisRight.textColor = ContextCompat.getColor(this.context!!, R.color.table_text_color)
        //yAxisRight.setCenterAxisLabels(true)
        yAxisRight.labelCount = 4
        yAxisRight.axisMinimum = 1.0f
        yAxisRight.axisMaximum = 4.0f
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
        lineChart.setPadding(20)
    }
}
