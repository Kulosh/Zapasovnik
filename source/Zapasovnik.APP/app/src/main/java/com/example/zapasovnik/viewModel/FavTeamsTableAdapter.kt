package com.example.zapasovnik.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.FavPlayer
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.model.Team

class FavTeamsTableAdapter(
    private val teams: List<Team>,
    private val onTeamClick: (Team) -> Unit
):
    RecyclerView.Adapter<FavTeamsTableAdapter.FavTeamTableHolder>() {
        class FavTeamTableHolder(view: View): RecyclerView.ViewHolder(view) {
            val teamName: TextView = view.findViewById(R.id.FavTeamName)
            val row: LinearLayout = view.findViewById(R.id.teamRow)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavTeamTableHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fav_teams_table_row, parent, false)
            return FavTeamTableHolder(view)
        }

        override fun onBindViewHolder(holder: FavTeamTableHolder, position: Int) {
            val team = teams[position]
            holder.teamName.text = team.TeamName
            holder.row.setOnClickListener {
                onTeamClick(team)
            }
        }

        override fun getItemCount() = teams.size
}