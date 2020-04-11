package com.showtime.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.MyData
import kotlinx.android.synthetic.main.fragment_dashboard.*

class ViewPagerAdapter (
    var context: Context,
    var itemlist:ArrayList<MyData.Semester>,
    var listener:SemesterAdapter.SemesterAdapterListener
): RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.semester_page, parent, false)

        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return itemlist.size
    }
    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var data = itemlist[position]
        var adapter = SemesterAdapter(context, data.schedules, position, listener)
        var layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        holder.semester_recyclerView.layoutManager = layoutManager
        holder.semester_recyclerView.adapter = adapter
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var semester_recyclerView: RecyclerView
        init{
            semester_recyclerView = itemView.findViewById(R.id.semester_recyclerView)
        }
    }



}