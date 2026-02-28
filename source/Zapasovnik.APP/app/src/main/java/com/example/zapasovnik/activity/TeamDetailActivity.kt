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
import com.example.zapasovnik.model.TeamDetail
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import retrofit2.Response

class TeamDetailActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.team_detail_layout)

//        val isFav = intent.getBooleanExtra("isFav", false)
        val teamId = intent.getIntExtra("id", -1)
        val name = findViewById<TextView>(R.id.teamDetailName)
        val est = findViewById<TextView>(R.id.teamDetailEst)
        val delTeamBtn = findViewById<Button>(R.id.deleteTeamBtn)
        val favBtn = findViewById<Button>(R.id.addToFavTeams)
        val unfavBtn = findViewById<Button>(R.id.delFromFavTeams)
        var team: Response<TeamDetail> ?= null
        userData = UserData(this)
        val edit = findViewById<Button>(R.id.editTeamBtn)

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first().toInt())
                put("entityId", teamId)
            }
            team = RetrofitClient.api.postTeamDetail(user)
            val isFav = team.body()?.IsFavorite
            val loggedIn = userData.loggedInFlow.first().toBoolean()

            name.text = team.body()?.Name
            est.text = team.body()?.Established

            if (loggedIn)
            {
                if (isFav!!) {
                    favBtn.visibility = Button.GONE
                } else {
                    unfavBtn.visibility = Button.GONE
                }
            } else {
                favBtn.visibility = Button.GONE
                unfavBtn.visibility = Button.GONE
            }
        }

        delTeamBtn.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deleteTeam(teamId)

                if (resp.isSuccessful && resp.body() == true) {
                    val intent = Intent(this@TeamDetailActivity, HomeActivity::class.java)
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
                val favTeam = buildJsonObject {
                    put("teamId", teamId)
                    put("userId", userData.userIdFlow.first().toInt())
                }
                val resp = RetrofitClient.api.postAddFavTeam(favTeam)

                if (resp.isSuccessful && resp.body() == true){
                    finish()
                    startActivity(intent)
                }
            }
        }

        unfavBtn.setOnClickListener {
            lifecycleScope.launch {
                val favTeam = buildJsonObject {
                    put("teamId", teamId)
                    put("userId", userData.userIdFlow.first().toInt())
                }
                val resp = RetrofitClient.api.postDeleteFavTeam(favTeam)

                if (resp.isSuccessful && resp.body() == true) {
                    finish()
                    startActivity(intent)
                }
            }
        }

        edit.setOnClickListener {
            val intent = Intent(this, EditTeamActivity::class.java)
            intent.putExtra("teamId", teamId)
            startActivity(intent)
        }
    }
}