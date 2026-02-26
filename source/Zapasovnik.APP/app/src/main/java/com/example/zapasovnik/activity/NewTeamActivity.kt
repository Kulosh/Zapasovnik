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
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.time.LocalDateTime

class NewTeamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_team_layout)

        val addBtn = findViewById<Button>(R.id.newTeamAddBtn)
        var now = LocalDateTime.now().toString()
        val date = findViewById<CalendarView>(R.id.newTeamEst)

        date.setOnDateChangeListener { view, year, month, dayOfMonth ->
            now = "$year-${month + 1}-$dayOfMonth"
        }

        addBtn.setOnClickListener {
            val fname = findViewById<TextView>(R.id.newTeamName).text.toString()

//            Log.d("Birth", birth)
//            Log.d("Team", team)

            if (fname == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.new_player_enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, HomeActivity::class.java)

                lifecycleScope.launch {
                    val newTeamString = buildJsonObject {
                        put("TeamName", fname)
                        put("TeamEstablished", now)
                    }

//                    Log.d("String", newPlayerString.toString())

                    val resp = RetrofitClient.api.postAddTeam(newTeamString)
                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.new_team_success,
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