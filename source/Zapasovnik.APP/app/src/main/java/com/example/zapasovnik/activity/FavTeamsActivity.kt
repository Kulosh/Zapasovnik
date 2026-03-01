package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.FavPlayer
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.FavPlayersTableAdapter
import com.example.zapasovnik.viewModel.FavTeamsTableAdapter
import com.example.zapasovnik.viewModel.HomeMatchTableAdapter
import com.example.zapasovnik.viewModel.PlayersTableAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class FavTeamsActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // TODO: Change layout
        setContentView(R.layout.fav_teams_layout)

        userData = UserData(this)
        val recyclerView = findViewById<RecyclerView>(R.id.favTeamsTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val userId = buildJsonObject {
                put("userId", userData.userIdFlow.first())
            }
            val favTeams = RetrofitClient.api.postFavTeams(userId, "Bearer ${userData.jwtTokenFlow.first()}")

            recyclerView.adapter = FavTeamsTableAdapter(favTeams) { team ->
                val intent = Intent(this@FavTeamsActivity, TeamDetailActivity::class.java)
                intent.putExtra("id", team.TeamId)
                startActivity(intent)
            }
        }
    }
}