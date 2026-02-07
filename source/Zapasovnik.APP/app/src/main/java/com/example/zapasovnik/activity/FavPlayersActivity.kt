package com.example.zapasovnik.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R

class FavPlayersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fav_players_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.favPlayersTableView)
    }
}