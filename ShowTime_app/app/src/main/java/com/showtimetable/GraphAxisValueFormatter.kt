package com.showtimetable

import com.github.mikephil.charting.formatter.ValueFormatter


class GraphAxisValueFormatter(values:ArrayList<String>) : ValueFormatter() {
    val index = 0;
    var mValues = ArrayList<String>()

    init{
        mValues = values
    }

    override fun getFormattedValue(value: Float): String {
        if(mValues.size == 1){
            if(value != 0.0f)
                return ""
            else
                return mValues[0]
        }else{
            return mValues[value.toInt()]
        }
    }
//    var mValues = ArrayList<String>()
//
//    init{
//        mValues = values
//    }
//    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//        println("graph : "+mValues[value.toInt()])
//        return mValues[value.toInt()]
//    }
}