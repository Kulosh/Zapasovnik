package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.time.LocalDateTime
import kotlin.toString

class EditLeagueActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_league_layout)

        userData = UserData(this)

        val nameField = findViewById<TextView>(R.id.newLeagueName)
        val addBtn = findViewById<Button>(R.id.newLeagueAddBtn)
        val id = intent.getIntExtra("leagueId", -1)

        lifecycleScope.launch {
            val token = userData.jwtTokenFlow.first()
            val league = RetrofitClient.api.getLeagueDetail(id, "Bearer ${userData.jwtTokenFlow.first()}").body()
            nameField.text = league!!.LeagueName
        }

        addBtn.setOnClickListener {
            val name = nameField.text.toString()

//            Log.d("Birth", birth)
//            Log.d("Team", team)

            if (name == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.new_player_enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, HomeActivity::class.java)

                lifecycleScope.launch {
                    val token = userData.jwtTokenFlow.first()
                    val league = buildJsonObject {
                        put("LeagueName", name)
                    }

                    val resp = RetrofitClient.api.patchEditLeague(id, league, "Bearer ${userData.jwtTokenFlow.first()}")
                    if (resp.isSuccessful && resp.body()!!) {
                        Toast.makeText(
                            applicationContext,
                            R.string.new_league_success, //TODO: Added > Updated
                            Toast.LENGTH_SHORT
                        ).show()
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
}