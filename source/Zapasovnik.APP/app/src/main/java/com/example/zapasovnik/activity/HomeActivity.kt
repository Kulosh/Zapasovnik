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
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.HomeMatchTableAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_layout)

        userData = UserData(this)

        val recyclerView = findViewById<RecyclerView>(R.id.homeMatchTableView)
        val teamsButton = findViewById<Button>(R.id.teamBtn)
        val matchesButton = findViewById<Button>(R.id.matchBtn)
        val playersButton = findViewById<Button>(R.id.playerBtn)

        recyclerView.layoutManager = LinearLayoutManager(this)

        teamsButton.setOnClickListener {
            val intent = Intent(this, TeamsActivity::class.java)
            startActivity(intent)
        }

        matchesButton.setOnClickListener {
            val intent = Intent(this, MatchesActivity::class.java)
            startActivity(intent)
        }

        playersButton.setOnClickListener {
            val intent = Intent(this, PlayersActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val teamMatches: List<Match> = RetrofitClient.api.getTeamMatches()

            recyclerView.adapter = HomeMatchTableAdapter(teamMatches) { match ->
                val intent = Intent(this@HomeActivity, MatchDetailActivity::class.java)
                intent.putExtra("id", match.Id)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        userData = UserData(this)

        val loginButton = findViewById<ImageView>(R.id.loginIcon)

        val loginIntent = Intent(this, LoginActivity::class.java)
        val profileIntent = Intent(this, ProfileActivity::class.java)

        lifecycleScope.launch {
            val jwtExpTime = userData.jwtExpireFlow.first()
            var loginSuccess = userData.userIdFlow.first()

            if (loginSuccess != -1 && jwtExpTime < System.currentTimeMillis()/1000 + 60) {
                userData.storeUser(-1, "", "", "", 0, false)
                loginSuccess = -1
            }

            if (loginSuccess != -1) {
                loginButton.setOnClickListener {
                    startActivity(profileIntent)
                }
            } else {
                loginButton.setOnClickListener {
                    startActivity(loginIntent)
                }
            }
        }
    }
}