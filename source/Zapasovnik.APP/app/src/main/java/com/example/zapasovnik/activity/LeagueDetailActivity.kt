package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LeagueDetailActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.league_detail_layout)

        userData = UserData(this)

        val id = intent.getIntExtra("id", -1)
        val nameView = findViewById<TextView>(R.id.leagueDetailName)
        val deleteButton = findViewById<Button>(R.id.leagueDetailDeleteBtn)
        val editButton = findViewById<Button>(R.id.leagueDetailEditBtn)

        lifecycleScope.launch {
            val league = RetrofitClient.api.getLeagueDetail(id, "Bearer ${userData.jwtTokenFlow.first()}")

            nameView.text = league.body()?.LeagueName
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deleteLeague(id, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true) {
                    val intent = Intent(this@LeagueDetailActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditLeagueActivity::class.java)
            intent.putExtra("leagueId", id)
            startActivity(intent)
        }
    }
}