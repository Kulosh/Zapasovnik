package com.example.zapasovnik.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.FavPlayer
import com.example.zapasovnik.model.League
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.model.Team

class LeaguesTableAdapter(
    private val leagues: List<League>,
    private val onTeamClick: (League) -> Unit
):
    RecyclerView.Adapter<LeaguesTableAdapter.LeagueTableHolder>() {
        class LeagueTableHolder(view: View): RecyclerView.ViewHolder(view) {
            val leagueName: TextView = view.findViewById(R.id.LeagueName)
            val row: LinearLayout = view.findViewById(R.id.leagueRow)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueTableHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.leagues_table_row, parent, false)
            return LeagueTableHolder(view)
        }

        override fun onBindViewHolder(holder: LeagueTableHolder, position: Int) {
            val league = leagues[position]
            holder.leagueName.text = league.LeagueName
            holder.row.setOnClickListener {
                onTeamClick(league)
            }
        }

        override fun getItemCount() = leagues.size
}