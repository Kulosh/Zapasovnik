package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_layout)

        val loginSuccess =  intent.getBooleanExtra("success", false)

        val recyclerView = findViewById<RecyclerView>(R.id.homeMatchTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val playersClick = findViewById<Button>(R.id.playerBtn)
        playersClick.setOnClickListener {
            val intent = Intent(this, PlayersActivity::class.java)
            startActivity(intent)
        }

        if (loginSuccess) {
            val loginClick = findViewById<ImageView>(R.id.loginIcon)
            loginClick.setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        } else {
            val loginClick = findViewById<ImageView>(R.id.loginIcon)
            loginClick.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        lifecycleScope.launch {
            try {
                val teamMatches: List<Match> = RetrofitClient.api.getTeamMatches()
                recyclerView.adapter = HomeMatchTableAdapter(teamMatches)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}