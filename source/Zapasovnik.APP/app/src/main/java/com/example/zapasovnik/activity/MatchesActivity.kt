package com.example.zapasovnik.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.HomeMatchTableAdapter
import kotlinx.coroutines.launch

class MatchesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.matches_list_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.matchesTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val teamMatches: List<Match> = RetrofitClient.api.getTeamMatches()
            recyclerView.adapter = HomeMatchTableAdapter(teamMatches)
        }
    }
}