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
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PlayerDetailActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.player_detail_layout)

        userData = UserData(this)

        val id = intent.getIntExtra("id", -1)
        val fnameView = findViewById<TextView>(R.id.playerDetaliFname)
        val lnameView = findViewById<TextView>(R.id.playerDetailLname)
        val birthView = findViewById<TextView>(R.id.playerDetailBirth)
        val teamView = findViewById<TextView>(R.id.playerDetailTeam)
        val deleteButton = findViewById<Button>(R.id.deletePlayerBtn)
        val favButton = findViewById<Button>(R.id.addToFavPlayers)
        val unfavButton = findViewById<Button>(R.id.delFromFavPlayers)
        val editButton = findViewById<Button>(R.id.editPlayerBtn)

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first())
                put("entityId", id)
            }

            val player = RetrofitClient.api.postPlayerDetail(user)

            val isFav = player.body()!!.IsFavorite
            val loggedIn = userData.userIdFlow.first()
            val isAdmin = userData.adminFlow.first()

            if (loggedIn != -1  && !isAdmin) {
                if (isFav) favButton.visibility = Button.GONE else unfavButton.visibility = Button.GONE
            } else {
                favButton.visibility = Button.GONE
                unfavButton.visibility = Button.GONE
            }

            if (loggedIn == -1 || !isAdmin) {
                editButton.visibility = Button.GONE
                deleteButton.visibility = Button.GONE
            }

            fnameView.text = player.body()!!.FName
            lnameView.text = player.body()!!.LName
            birthView.text = player.body()!!.Birth
            teamView.text = player.body()!!.Team
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deletePlayer(id, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true) {
                    val intent = Intent(this@PlayerDetailActivity, HomeActivity::class.java)
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

        favButton.setOnClickListener {
            lifecycleScope.launch {
                val favPlayer = buildJsonObject {
                    put("entityId", id)
                    put("userId", userData.userIdFlow.first())
                }

                val resp = RetrofitClient.api.postAddFavPlayer(favPlayer, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true){
                    finish()
                    startActivity(intent)
                }
            }
        }

        unfavButton.setOnClickListener {
            lifecycleScope.launch {
                val favPlayer = buildJsonObject {
                    put("entityId", id)
                    put("userId", userData.userIdFlow.first())
                }
                val resp = RetrofitClient.api.postDeleteFavPlayer(favPlayer, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true) {
                    finish()
                    startActivity(intent)
                }
            }
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditPlayerActivity::class.java)
            intent.putExtra("playerId", id)
            startActivity(intent)
        }
    }
}