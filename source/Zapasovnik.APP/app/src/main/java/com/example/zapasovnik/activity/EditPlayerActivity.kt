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

class EditPlayerActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_player_layout)

        userData = UserData(this)
        val id = intent.getIntExtra("playerId", -1)

        val teamSel = findViewById<AutoCompleteTextView>(R.id.newPlayerTeam)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        teamSel.setAdapter(adapter)
        val addBtn = findViewById<Button>(R.id.newPlayerAddBtn)
        var birth = LocalDateTime.now().toString()
        val date = findViewById<CalendarView>(R.id.newPlayerBorn)
        val fname = findViewById<TextView>(R.id.newPlayerFName)
        val lname = findViewById<TextView>(R.id.newPlayerLName)

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first().toInt())
                put("entityId", id)
            }
            val player = RetrofitClient.api.postPlayerDetail(user)

            fname.text = player.body()?.FName
            lname.text = player.body()?.LName
            teamSel.setText(player.body()?.Team)

            val oldDate = player.body()?.Birth?.replace(Regex("[\\p{Z}\\s]+"), " ")
            val formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a", Locale.US)
            val ldt = LocalDateTime.parse(oldDate, formatter)
            val millis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            date.date = millis
        }

        teamSel.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getTeams().map { it.TeamName }
//                    Log.d("Teams", "$teams")

                    adapter.clear()
                    adapter.addAll(teams)
                    adapter.notifyDataSetChanged()

                    if (teams.isNotEmpty() && !teamSel.isPopupShowing) {
                        teamSel.showDropDown()
                    }
                }
            }
        })

        date.setOnDateChangeListener { view, year, month, dayOfMonth ->
            birth = "$year-${month + 1}-$dayOfMonth"
        }

        addBtn.setOnClickListener {
            val team = teamSel.text.toString()

//            Log.d("Birth", birth)
//            Log.d("Team", team)

            if (fname.text == "" || lname.text == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.new_player_enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, HomeActivity::class.java)

                lifecycleScope.launch {
                    val newPlayerString = buildJsonObject {
                        put("FName", fname.text.toString())
                        put("LName", lname.text.toString())
                        put("Birth", birth)
                        put("Team", team)
                    }

//                    Log.d("String", newPlayerString.toString())

                    val resp = RetrofitClient.api.patchEditPlayer(id,newPlayerString, "Bearer ${userData.jwtTokenFlow.first()}")
                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.new_player_success,
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