package com.example.zapasovnik.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.FavPlayer

class FavPlayersTableAdapter(private val players: List<FavPlayer>):
    RecyclerView.Adapter<FavPlayersTableAdapter.FavPlayerTableHolder>() {
        class FavPlayerTableHolder(view: View): RecyclerView.ViewHolder(view) {
            val fname: TextView = view.findViewById(R.id.FavPlayerFname)
            val lname: TextView = view.findViewById(R.id.FavPlayerLname)
            val team: TextView = view.findViewById(R.id.FavPlayerTeam)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavPlayerTableHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fav_players_table_row, parent, false)
            return FavPlayerTableHolder(view)
        }

        override fun onBindViewHolder(holder: FavPlayerTableHolder, position: Int) {
            val player = players[position]
            holder.fname.text = player.FName
            holder.lname.text = player.LName
            holder.team.text = player.Team
        }

        override fun getItemCount() = players.size
}