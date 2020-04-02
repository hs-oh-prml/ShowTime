package com.showtime.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.LectureItem

class ComputeScoreAdapter (
    val context: Context,
    var items:ArrayList<LectureItem>,
    var scoreList:ArrayList<Double>,
    var listener: scoreListener
): RecyclerView.Adapter<ComputeScoreAdapter.ViewHolder>() {

    interface scoreListener{
        fun chcekScore()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_compute_score, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        if (items == null)
            return 0
        return items.size
    }

    var lastSelectedPosition = -1


    var score = ""

    fun getScore():ArrayList<Double> = scoreList

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listener.chcekScore()
        holder.setIsRecyclable(false)
        val data = items[position]
        holder.lectName.text = data.lectName
        holder.scoreView.setOnCheckedChangeListener { radioGroup, i ->
            score = radioGroup.findViewById<RadioButton>(i).text.toString()
            when(score){
                "A+"->{
                    scoreList[position] = 4.5
                }
                "A"->{
                    scoreList[position] = 4.0
                }
                "B+"->{
                    scoreList[position] = 3.5
                }
                "B"->{
                    scoreList[position] = 3.0
                }
                "C+"->{
                    scoreList[position] = 2.5
                }
                "C"->{
                    scoreList[position] = 2.0
                }
                "D+"->{
                    scoreList[position] = 1.5
                }
                "D"->{
                    scoreList[position] = 1.0
                }
                "F"->{
                    scoreList[position] = 0.0
                }
                "P"->{
                    scoreList[position] = -1.0
                }
                "NP"->{
                    scoreList[position] = -1.0
                }
            }
            listener.chcekScore()
            Log.v("score", score)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var lectName: TextView
        var scoreView: RadioGroup

        init {
            lectName = itemView.findViewById(R.id.name)
            scoreView = itemView.findViewById(R.id.score)
            var scoreArray = context.resources.getStringArray(R.array.score)
            for((index, i) in scoreArray.withIndex()){
                var rb = RadioButton(context)
                rb.setTextColor(context.getColor(R.color.white))
                rb.text = i
                rb.gravity = CENTER
                rb.id = index
                rb.setButtonDrawable(android.R.color.transparent)
                rb.setBackgroundResource(R.drawable.selector)
                scoreView.addView(rb)
            }
            scoreView.check(0)

        }
    }

}