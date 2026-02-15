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
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch

class PlayerDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.player_detail_layout)

        val playerId = intent.getIntExtra("id", -1)
        val fname = findViewById<TextView>(R.id.playerDetaliFname)
        val lname = findViewById<TextView>(R.id.playerDetailLname)
        val birth = findViewById<TextView>(R.id.playerDetailBirth)
        val team = findViewById<TextView>(R.id.playerDetailTeam)
        val delPlayerBtn = findViewById<Button>(R.id.deletePlayerBtn)

        lifecycleScope.launch {
            val player = RetrofitClient.api.getPlayerDetail(playerId)

            fname.text = player.body()?.FName
            lname.text = player.body()?.LName
            birth.text = player.body()?.Birth
            team.text = player.body()?.Team
        }

        delPlayerBtn.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deletePlayer(playerId)

                if (resp.isSuccessful && resp.body() == true) {
                    val intent = Intent(this@PlayerDetailActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.network_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}