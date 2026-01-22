package com.example.zapasovnik.viewModel


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.R

class HomeMatchTableAdapter(private val match: List<Match>):
    RecyclerView.Adapter<HomeMatchTableAdapter.HomeMatchTableHolder>() {
    class HomeMatchTableHolder(view: View): RecyclerView.ViewHolder(view) {
        val team1: TextView = view.findViewById(R.id.Team1)
        val date: TextView = view.findViewById(R.id.Date)
        val team2: TextView = view.findViewById(R.id.Team2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMatchTableHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_match_table_row, parent, false)
        return HomeMatchTableHolder(view)
    }

    override fun onBindViewHolder(holder: HomeMatchTableHolder, position: Int) {
        val match = match[position]
        holder.team1.text = match.Team1
        holder.date.text = match.Date
        holder.team2.text = match.Team2
    }

    override fun getItemCount() = match.size

}

//class HomeMatchTableAdapter(private var matchList: List<Match>) :
//    RecyclerView.Adapter<HomeMatchTableAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
//        val v: View = LayoutInflater.from(viewGroup.context)
//            .inflate(R.layout.home_match_table_row, viewGroup, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
//        viewHolder.team1.text = matchList[i].Team1
//        viewHolder.date.text = matchList[i].Date
//        viewHolder.team2.text = matchList[i].Team2
//    }
//
//    override fun getItemCount(): Int {
//        return matchList.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val team1: TextView = itemView.findViewById(R.id.Team1)
//        val date: TextView = itemView.findViewById(R.id.Date)
//        val team2: TextView = itemView.findViewById(R.id.Team2)
//
//
//    }
//}