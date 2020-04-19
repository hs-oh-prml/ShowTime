package com.showtime.ui.home

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.showtime.R
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.timetable.TableFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_table.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class HomeFragment : Fragment()    {
    lateinit var pref:PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = PreferenceManager(context!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }
    fun init(){
        
        childFragmentManager.beginTransaction().replace(R.id.table, TableFragment(context!!, pref.table)).commit()
//        if(pref.getMainTable() == null){
//            var thread = object: Thread(){
//                override fun run() {
//                    super.run()
//                    sleep(1000)
//                    screenCapture()
//                }
//            }
//            thread.start()
//        }
    }

    override fun onResume() {
        childFragmentManager.beginTransaction().replace(R.id.table, TableFragment(context!!, pref.table)).commit()
        super.onResume()

    }
}