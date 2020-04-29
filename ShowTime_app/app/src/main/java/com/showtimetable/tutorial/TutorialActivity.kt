package com.showtimetable.tutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.showtimetable.R
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)


        var adapter = TutorialAdapter(this)
        viewpager.adapter = adapter
        indicator.setViewPager(viewpager)

        exit.setOnClickListener {
            var index = viewpager.currentItem
            if(index == 6){
                finish()
            } else{
                if(index == 5){
                    exit.text = "Show Time 시작하기!"
                }
                viewpager.setCurrentItem(index + 1, true)
            }
        }
    }
}
