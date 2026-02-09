package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        val loginIntent = Intent(this, LoginActivity::class.java)
        val profileIntent = Intent(this, ProfileActivity::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.homeMatchTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val playersClick = findViewById<Button>(R.id.playerBtn)
        playersClick.setOnClickListener {
            val intent = Intent(this, PlayersActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val loginSuccess = userData.loggedInFlow.first()
//            Log.e("Login success", loginSuccess)

            if (loginSuccess == "true") {
                val loginClick = findViewById<ImageView>(R.id.loginIcon)
                loginClick.setOnClickListener {
                    startActivity(profileIntent)
                }
            } else {
                val loginClick = findViewById<ImageView>(R.id.loginIcon)
                loginClick.setOnClickListener {
                    startActivity(loginIntent)
                }
            }

            try {
                val teamMatches: List<Match> = RetrofitClient.api.getTeamMatches()
                recyclerView.adapter = HomeMatchTableAdapter(teamMatches)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}