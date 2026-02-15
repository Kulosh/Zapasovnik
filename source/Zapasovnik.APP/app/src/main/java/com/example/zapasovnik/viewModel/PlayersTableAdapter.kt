package com.example.zapasovnik.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.FavPlayer

class PlayersTableAdapter(
    private val players: List<FavPlayer>,
    private val onPlayerClick: (FavPlayer) -> Unit
) : RecyclerView.Adapter<PlayersTableAdapter.PlayerTableHolder>() {

    class PlayerTableHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fname: TextView = view.findViewById(R.id.FavPlayerFname)
        val lname: TextView = view.findViewById(R.id.FavPlayerLname)
        val team: TextView = view.findViewById(R.id.FavPlayerTeam)
        val row: LinearLayout = view.findViewById(R.id.playerRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerTableHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_players_table_row, parent, false)
        return PlayerTableHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerTableHolder, position: Int) {
        val player = players[position]
        holder.fname.text = player.FName
        holder.lname.text = player.LName
        holder.team.text = player.Team
        holder.row.setOnClickListener {
            onPlayerClick(player)
        }
    }

    override fun getItemCount() = players.size
}