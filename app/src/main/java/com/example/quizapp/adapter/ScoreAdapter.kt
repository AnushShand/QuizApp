package com.example.quizapp.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.database.HighScore

class ScoreAdapter(private val context:Context,private val data:List<HighScore>,private val currentId:Long):RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>()
{
    class ScoreViewHolder(private val view:View):RecyclerView.ViewHolder(view)
    {
        val nameView:TextView=view.findViewById(R.id.name_text)
        val scoreView:TextView=view.findViewById(R.id.score_text)
        val timeView:TextView=view.findViewById(R.id.time_taken_text)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val item=data[position]
        if(currentId==item.id) {

            holder.scoreView.setBackgroundColor(Color.parseColor("#FF03DAC5"))
            holder.nameView.setBackgroundColor(Color.parseColor("#FF03DAC5"))
            holder.timeView.setBackgroundColor(Color.parseColor("#FF03DAC5"))
        }
        else{
            holder.scoreView.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            holder.nameView.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            holder.timeView.setBackgroundColor(Color.parseColor("#FFFFFFFF"))

        }
        holder.scoreView.text=item.score.toString()
        holder.nameView.text=item.name
        holder.timeView.text=item.timeTaken.toString()
    }

    override fun getItemCount()=data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder
    {
        val adapterLayout=LayoutInflater.from(parent.context).inflate(R.layout.score_item,parent,false)
        return ScoreViewHolder(adapterLayout)
    }
}