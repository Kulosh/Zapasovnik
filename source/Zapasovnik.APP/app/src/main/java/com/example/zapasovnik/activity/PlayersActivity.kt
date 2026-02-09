package com.example.zapasovnik.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.PlayersTableAdapter
import kotlinx.coroutines.launch

class PlayersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.players_list_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.playersTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val players = RetrofitClient.api.getPlayers()

            recyclerView.adapter = PlayersTableAdapter(players)
        }
    }
}