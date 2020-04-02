package com.showtime.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R
import com.showtime.data.MajorItem

class MajorRecyclerViewAdapter(
    val context: Context,
    var items:ArrayList<MajorItem>,
    var listener: MajorAdapterListener
): RecyclerView.Adapter<MajorRecyclerViewAdapter.ViewHolder>() {

    interface MajorAdapterListener{
        fun itemOnClick(position:Int)
        fun selectMajor(item:MajorItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_major, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        if (items == null)
            return 0
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = items[position]
        Log.d("Data Size", data.toString())

        if(data.type == 0){
            holder.name.text = data.depart
            holder.child.visibility = VISIBLE
        } else {
            holder.child.visibility = INVISIBLE
            holder.name.text = data.major
        }

        holder.itemView.setOnClickListener {
            if(data.type == 1){
                listener.selectMajor(data)
            } else {
                listener.itemOnClick(position)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var child:ImageView
        init {
            name = itemView.findViewById(R.id.m_name)
            child = itemView.findViewById(R.id.child)
        }
    }

}