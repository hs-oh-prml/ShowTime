package com.showtime

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.custom_toast.view.*

class CustomToast(
    val context: Context,
    val text:String
) {
    var t_view:View
    var inflater:LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
        t_view = inflater.inflate(R.layout.custom_toast,null, false)
        t_view.toast_text.text = text
    }

    fun show(){
        val toast = Toast(this.context)
        //toast.setGravity(Gravity.BOTTOM,0,15)
        toast.view = t_view
        toast.show()
    }
}