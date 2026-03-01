package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.model.PlayerDetail
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import retrofit2.Response

class PlayerDetailActivity : ComponentActivity() {

    lateinit var userData: UserData

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
        val favBtn = findViewById<Button>(R.id.addToFavPlayers)
        val unfavBtn = findViewById<Button>(R.id.delFromFavPlayers)
        var player: Response<PlayerDetail> ?= null
        userData = UserData(this)
        val edit = findViewById<Button>(R.id.editPlayerBtn)

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first().toInt())
                put("entityId", playerId)
            }
            player = RetrofitClient.api.postPlayerDetail(user)

            val isFav = player.body()?.IsFavorite
            val loggedIn = userData.userIdFlow.first()

            if (loggedIn != -1) {
                if (isFav!!) favBtn.visibility = Button.GONE else unfavBtn.visibility = Button.GONE
            } else {
                favBtn.visibility = Button.GONE
                unfavBtn.visibility = Button.GONE
            }

            fname.text = player.body()?.FName
            lname.text = player.body()?.LName
            birth.text = player.body()?.Birth
            team.text = player.body()?.Team
        }

        delPlayerBtn.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deletePlayer(playerId, "Bearer ${userData.jwtTokenFlow.first()}")

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

        favBtn.setOnClickListener {
            lifecycleScope.launch {
                val favPlayer = buildJsonObject {
                    put("playerId", player?.body()?.Id)
                    put("userId", userData.userIdFlow.first())
                }
//                Log.d("String", "Bearer ${userData.jwtTokenFlow.first()}")

                val resp = RetrofitClient.api.postAddFavPlayer(favPlayer, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true){
                    finish()
                    startActivity(intent)
                }
            }
        }

        unfavBtn.setOnClickListener {
            lifecycleScope.launch {
                val favPlayer = buildJsonObject {
                    put("playerId", player?.body()?.Id)
                    put("userId", userData.userIdFlow.first())
                }
                val resp = RetrofitClient.api.postDeleteFavPlayer(favPlayer, "Bearer ${userData.jwtTokenFlow.first()}")
//                Log.d("String", favPlayer.toString())
//                Log.d("Response", resp.toString())

                if (resp.isSuccessful && resp.body() == true) {
                    finish()
                    startActivity(intent)
                }
            }
        }

        edit.setOnClickListener {
            val intent = Intent(this, EditPlayerActivity::class.java)
            intent.putExtra("playerId", playerId)
            startActivity(intent)
        }
    }
}