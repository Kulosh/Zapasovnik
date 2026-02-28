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
import com.example.zapasovnik.model.MatchDetail
import com.example.zapasovnik.model.PlayerDetail
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

        val matchId = intent.getIntExtra("id", -1)
        val team1 = findViewById<TextView>(R.id.matchDetailTeam1)
        val team2 = findViewById<TextView>(R.id.matchDetailTeam2)
        val date = findViewById<TextView>(R.id.matchDetailDate)
        val league = findViewById<TextView>(R.id.matchDetailLeague)
        val delMatchBtn = findViewById<Button>(R.id.deleteMatchBtn)
        val favBtn = findViewById<Button>(R.id.addToFavMatches)
        val unfavBtn = findViewById<Button>(R.id.delFromFavMatches)
        var match: Response<MatchDetail> ?= null
        userData = UserData(this)
        val edit = findViewById<Button>(R.id.editMatchBtn)

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first().toInt())
                put("entityId", matchId)
            }
            match = RetrofitClient.api.postMatchDetail(user)

            val isFav = match.body()?.IsFavorite
            val loggedIn = userData.loggedInFlow.first().toBoolean()

            if (loggedIn) {
                if (isFav!!) favBtn.visibility = Button.GONE else unfavBtn.visibility = Button.GONE
            } else {
                favBtn.visibility = Button.GONE
                unfavBtn.visibility = Button.GONE
            }

            team1.text = match.body()?.Team1
            team2.text = match.body()?.Team2
            date.text = match.body()?.Date
            league.text = match.body()?.League
        }

        delMatchBtn.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deleteMatch(matchId)

                if (resp.isSuccessful && resp.body() == true) {
                    val intent = Intent(this@MatchDetailActivity, HomeActivity::class.java)
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
                val favMatch = buildJsonObject {
                    put("matchId", match?.body()?.Id)
                    put("userId", userData.userIdFlow.first().toInt())
                }
//                     TODO: PostFav
                val resp = RetrofitClient.api.postAddFavMatch(favMatch)

                if (resp.isSuccessful && resp.body() == true){
                    finish()
                    startActivity(intent)
                }
            }
        }

        unfavBtn.setOnClickListener {
            lifecycleScope.launch {
                val favMatch = buildJsonObject {
                    put("matchId", match?.body()?.Id)
                    put("userId", userData.userIdFlow.first().toInt())
                }
                    // TODO: PostUnFav
                val resp = RetrofitClient.api.postDeleteFavMatch(favMatch)
//                Log.d("String", favPlayer.toString())
//                Log.d("Response", resp.toString())

                if (resp.isSuccessful && resp.body() == true) {
                    finish()
                    startActivity(intent)
                }
            }
        }

        edit.setOnClickListener {
            val intent = Intent(this, EditMatchActivity::class.java)
            intent.putExtra("matchId", matchId)
            startActivity(intent)
        }
    }
}