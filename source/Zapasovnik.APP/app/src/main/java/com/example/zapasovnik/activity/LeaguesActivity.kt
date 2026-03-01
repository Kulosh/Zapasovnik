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
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.FavTeamsTableAdapter
import com.example.zapasovnik.viewModel.LeaguesTableAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LeaguesActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.leagues_list_layout)

        userData = UserData(this)

        val recyclerView = findViewById<RecyclerView>(R.id.leaguesTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val addLeague = findViewById<Button>(R.id.addLeagueBtn)

        addLeague.setOnClickListener {
            val intent = Intent(this, NewLeagueActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val leagues = RetrofitClient.api.getLeagues("Bearer ${userData.jwtTokenFlow.first()}")

            recyclerView.adapter = LeaguesTableAdapter(leagues) { league ->
                val intent = Intent(this@LeaguesActivity, LeagueDetailActivity::class.java)
                intent.putExtra("id", league.LeagueId)
                startActivity(intent)
            }
        }
    }
}