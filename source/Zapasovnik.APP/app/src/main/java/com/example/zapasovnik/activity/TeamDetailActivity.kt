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

class TeamDetailActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.team_detail_layout)

        userData = UserData(this)

        val id = intent.getIntExtra("id", -1)
        val nameView = findViewById<TextView>(R.id.teamDetailName)
        val estView = findViewById<TextView>(R.id.teamDetailEst)
        val deleteButton = findViewById<Button>(R.id.teamDetailDeleteBtn)
        val favButton = findViewById<Button>(R.id.teamDetailAddFavBtn)
        val unfavButton = findViewById<Button>(R.id.teamDetailDeleteFavBtn)
        val editButton = findViewById<Button>(R.id.teamDetailEditBtn)

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first())
                put("entityId", id)
            }

            val team = RetrofitClient.api.postTeamDetail(user)

            val isFav = team.body()!!.IsFavorite
            val loggedIn = userData.userIdFlow.first()
            val isAdmin = userData.adminFlow.first()

            nameView.text = team.body()?.Name
            estView.text = team.body()?.Established

            if (loggedIn != -1 && !isAdmin)
            {
                if (isFav) {
                    favButton.visibility = Button.GONE
                } else {
                    unfavButton.visibility = Button.GONE
                }
            } else {
                favButton.visibility = Button.GONE
                unfavButton.visibility = Button.GONE
            }

            if (loggedIn == -1 || !isAdmin) {
                editButton.visibility = Button.GONE
                deleteButton.visibility = Button.GONE
            }
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deleteTeam(id, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true) {
                    val intent = Intent(this@TeamDetailActivity, HomeActivity::class.java)
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
                val favTeam = buildJsonObject {
                    put("entityId", id)
                    put("userId", userData.userIdFlow.first())
                }
                val resp = RetrofitClient.api.postAddFavTeam(favTeam, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true){
                    finish()
                    startActivity(intent)
                }
            }
        }

        unfavButton.setOnClickListener {
            lifecycleScope.launch {
                val favTeam = buildJsonObject {
                    put("entityId", id)
                    put("userId", userData.userIdFlow.first())
                }
                val resp = RetrofitClient.api.postDeleteFavTeam(favTeam, "Bearer ${userData.jwtTokenFlow.first()}")

                if (resp.isSuccessful && resp.body() == true) {
                    finish()
                    startActivity(intent)
                }
            }
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditTeamActivity::class.java)
            intent.putExtra("teamId", id)
            startActivity(intent)
        }
    }
}