package com.showtime.tutorial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.showtime.R

class TutorialAdapter (var context: Context): RecyclerView.Adapter<TutorialAdapter.ViewHolder>()
{
    val page_list = arrayOf(
        R.drawable.slide1,
        R.drawable.slide2,
        R.drawable.slide3,
        R.drawable.slide4,
        R.drawable.slide5,
        R.drawable.slide6,
        R.drawable.slide7
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(context)
            .inflate(R.layout.tutorial_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return page_list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        holder.imageView.setImageResource(page_list[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageView: ImageView
        init{
            imageView =itemView.findViewById(R.id.image_view)
        }
    }

}