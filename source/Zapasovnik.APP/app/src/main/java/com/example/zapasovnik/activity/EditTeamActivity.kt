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
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class EditTeamActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_team_layout)

        userData = UserData(this)
        val id = intent.getIntExtra("teamId", -1)

        val addBtn = findViewById<Button>(R.id.newTeamAddBtn)
        var now = LocalDateTime.now().toString()
        val date = findViewById<CalendarView>(R.id.newTeamEst)
        val fname = findViewById<TextView>(R.id.newTeamName)

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first().toInt())
                put("entityId", id)
            }
            val team = RetrofitClient.api.postTeamDetail(user)

            fname.setText(team.body()?.Name)
            val oldDate = team.body()?.Established?.replace(Regex("[\\p{Z}\\s]+"), " ")
            val formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a", Locale.US)
            val ldt = LocalDateTime.parse(oldDate, formatter)
            val millis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            date.date = millis
        }

        date.setOnDateChangeListener { view, year, month, dayOfMonth ->
            now = "$year-${month + 1}-$dayOfMonth"
        }

        addBtn.setOnClickListener {
//            Log.d("Birth", birth)
//            Log.d("Team", team)

            if (fname.text == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.new_player_enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, HomeActivity::class.java)

                lifecycleScope.launch {
                    val newTeamString = buildJsonObject {
                        put("TeamName", fname.text.toString())
                        put("TeamEstablished", now)
                    }

//                    Log.d("String", newPlayerString.toString())

                    val resp = RetrofitClient.api.patchEditTeam(id,newTeamString)
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