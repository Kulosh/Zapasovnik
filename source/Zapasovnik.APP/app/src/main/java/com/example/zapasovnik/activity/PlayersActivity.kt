package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        val addPlayer = findViewById<Button>(R.id.addPlayerBtn)

        addPlayer.setOnClickListener {
            val intent = Intent(this, NewPlayerActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val players = RetrofitClient.api.getPlayers()

            recyclerView.adapter = PlayersTableAdapter(players)
        }
    }
}