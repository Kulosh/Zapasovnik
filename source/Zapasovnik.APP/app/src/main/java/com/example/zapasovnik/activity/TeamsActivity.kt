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
import com.example.zapasovnik.viewModel.FavTeamsTableAdapter
import com.example.zapasovnik.viewModel.PlayersTableAdapter
import kotlinx.coroutines.launch

class TeamsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.teams_list_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.teamsTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val addTeam = findViewById<Button>(R.id.addTeamBtn)

        addTeam.setOnClickListener {
            val intent = Intent(this, NewTeamActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val teams = RetrofitClient.api.getTeams()

            recyclerView.adapter = FavTeamsTableAdapter(teams) { team ->
                val intent = Intent(this@TeamsActivity, TeamDetailActivity::class.java)
                intent.putExtra("id", team.TeamId)
                startActivity(intent)
            }
        }
    }
}