package com.example.zapasovnik.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.Match

class HomeMatchTableAdapter(
    private val matches: List<Match>,
    private val onMatchClick: (Match) -> Unit
) : RecyclerView.Adapter<HomeMatchTableAdapter.HomeMatchTableHolder>() {

    class HomeMatchTableHolder(view: View): RecyclerView.ViewHolder(view) {
        val team1: TextView = view.findViewById(R.id.Team1)
        val date: TextView = view.findViewById(R.id.Date)
        val team2: TextView = view.findViewById(R.id.Team2)
        val row: LinearLayout = view.findViewById(R.id.matchRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMatchTableHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_match_table_row, parent, false)
        return HomeMatchTableHolder(view)
    }

    override fun onBindViewHolder(holder: HomeMatchTableHolder, position: Int) {
        val match = matches[position]
        holder.team1.text = match.Team1
        holder.date.text = match.Date
        holder.team2.text = match.Team2
        holder.row.setOnClickListener {
            onMatchClick(match)
        }
    }

    override fun getItemCount() = matches.size
}