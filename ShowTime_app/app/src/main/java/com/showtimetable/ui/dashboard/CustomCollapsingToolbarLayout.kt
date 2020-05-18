package com.showtimetable.ui.dashboard

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.CollapsingToolbarLayout

class CustomCollapsingToolbarLayout : CollapsingToolbarLayout {

    private var mListener :Listener?=null

    interface Listener{
        fun onContentScrimAnimationStarted(show:Boolean)
    }

    constructor(context:Context):super(context)

    constructor(context: Context,attrs: AttributeSet) : super(context,attrs)

    constructor(context: Context,attrs: AttributeSet,defStyleAttr:Int):super(context,attrs,defStyleAttr)

    override fun setScrimsShown(shown: Boolean, animate: Boolean) {
        super.setScrimsShown(shown, animate)
        if(animate && mListener != null){
            mListener!!.onContentScrimAnimationStarted(shown)
        }
    }

    fun setListener(listener:Listener){
        mListener = listener
    }
}