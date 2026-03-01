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

class NewLeagueActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_league_layout)

        userData = UserData(this)
        val addBtn = findViewById<Button>(R.id.newLeagueAddBtn)

        addBtn.setOnClickListener {
            val name = findViewById<TextView>(R.id.newLeagueName).text.toString()

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
//                    Log.d("String", newPlayerString.toString())

                    val league = buildJsonObject {
                        put("LeagueName", name)
                    }

                    val resp = RetrofitClient.api.postAddLeague(league, "Bearer ${userData.jwtTokenFlow.first()}")
                    if (resp.isSuccessful && resp.body()!!) {
                        Toast.makeText(
                            applicationContext,
                            R.string.new_league_success,
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