package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.FavTeamsTableAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavTeamsActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fav_teams_layout)

        userData = UserData(this)

        val recyclerView = findViewById<RecyclerView>(R.id.favTeamsTableView)

        lifecycleScope.launch {
            val userId = userData.userIdFlow.first()
            val favTeams = RetrofitClient.api.postFavTeams(userId, "Bearer ${userData.jwtTokenFlow.first()}")

            recyclerView.adapter = FavTeamsTableAdapter(favTeams) { team ->
                val intent = Intent(this@FavTeamsActivity, TeamDetailActivity::class.java)
                intent.putExtra("id", team.TeamId)
                startActivity(intent)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}