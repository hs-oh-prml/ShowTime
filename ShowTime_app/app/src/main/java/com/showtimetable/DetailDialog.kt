package com.showtimetable

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.addschedule.AddScheduleActivity
import com.showtimetable.data.MyData
import com.showtimetable.data.Schedule
import com.showtimetable.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_detail.*
import kotlinx.android.synthetic.main.dialog_detail.recycler_view


// 학기 클릭시 상세 정보 팝업
class DetailDialog(
    var c: Context,
    var tableNum:Int,
    var dListener: detailDialogDismissListener
) : DialogFragment() {

    lateinit var pref: PreferenceManager
    lateinit var info: MyData.Semester
    lateinit var data:ArrayList<Schedule>
    lateinit var adapter: DetailAdapter
    lateinit var listener: DetailAdapter.DetailListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.dialog_detail, container)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return v
    }

    interface detailDialogDismissListener{
        fun onDismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        val display = activity!!.windowManager.defaultDisplay // in case of Fragment
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        init()
    }

    fun init(){

//        //다이얼로그 밖의 화면은 흐리게 만들어줌
        pref = PreferenceManager(c)
        info = pref.myData.semester[tableNum]
        data = info.schedules
        var layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        listener = object: DetailAdapter.DetailListener {
            override fun refresh() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                init()
            }
        }

        adapter = DetailAdapter(c, data, tableNum, listener)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter




        title.text = info.semester
        var _credit = 0
        var _count = 0
        for(i in info.schedules){
            if(i.isLecture){
                _count++
                _credit += i.credit
            }
        }
        total_credit.text = "${_credit}학점"
        lecture_count.text = "${_count} 과목"

        add.setOnClickListener {
            var intent = Intent(c, AddScheduleActivity::class.java)
            intent.putExtra("tableNum", tableNum)
            c.startActivity(intent)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dListener.onDismiss()
    }
}
