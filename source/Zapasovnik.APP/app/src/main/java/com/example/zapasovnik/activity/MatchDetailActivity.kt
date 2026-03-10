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
import com.example.zapasovnik.model.MatchDetail
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import retrofit2.Response

class MatchDetailActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.match_detail_layout)

        userData = UserData(this)

        val id = intent.getIntExtra("id", -1)
        val team1View = findViewById<TextView>(R.id.matchDetailTeam1)
        val team2View = findViewById<TextView>(R.id.matchDetailTeam2)
        val dateView = findViewById<TextView>(R.id.matchDetailDate)
        val leagueView = findViewById<TextView>(R.id.matchDetailLeague)
        val deleteButton = findViewById<Button>(R.id.deleteMatchBtn)
        val favButton = findViewById<Button>(R.id.addToFavMatches)
        val unfavButton = findViewById<Button>(R.id.delFromFavMatches)
        val editButton = findViewById<Button>(R.id.editMatchBtn)

        var match: Response<MatchDetail> ?= null

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first())
                put("entityId", id)
            }

            match = RetrofitClient.api.postMatchDetail(user)

            val isFav = match.body()!!.IsFavorite
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

            team1View.text = match.body()!!.Team1
            team2View.text = match.body()!!.Team2
            dateView.text = match.body()!!.Date
            leagueView.text = match.body()!!.League
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deleteMatch(id, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true) {
                    val intent = Intent(this@MatchDetailActivity, HomeActivity::class.java)
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
                val favMatch = buildJsonObject {
                    put("entityId", match?.body()?.Id)
                    put("userId", userData.userIdFlow.first())
                }

                val resp = RetrofitClient.api.postAddFavMatch(favMatch, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true){
                    finish()
                    startActivity(intent)
                }
            }
        }

        unfavButton.setOnClickListener {
            lifecycleScope.launch {
                val favMatch = buildJsonObject {
                    put("entityId", match?.body()?.Id)
                    put("userId", userData.userIdFlow.first())
                }

                val resp = RetrofitClient.api.postDeleteFavMatch(favMatch, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true) {
                    finish()
                    startActivity(intent)
                }
            }
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditMatchActivity::class.java)
            intent.putExtra("matchId", id)
            startActivity(intent)
        }
    }
}