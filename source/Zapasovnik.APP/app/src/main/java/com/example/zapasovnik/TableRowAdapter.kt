package com.example.zapasovnik

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TableRowAdapter(private var userArrayList: ArrayList<Matches>) :
    RecyclerView.Adapter<TableRowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.matches_table_layout, viewGroup, false)
        return ViewHolder(v)
    }

//    TODO: Resolve tvUserName ???

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tvUserName.text = userArrayList[i].team1
        viewHolder.tvAge.text = userArrayList[i].date.toString()
        viewHolder.tvDesignation.text = userArrayList[i].team2
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.team1)
        val tvAge: TextView = itemView.findViewById(R.id.date)
        val tvDesignation: TextView = itemView.findViewById(R.id.team2)


    }
}