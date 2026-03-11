package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class NewPlayerActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_player_layout)

        userData = UserData(this)

        val fnameInput = findViewById<TextView>(R.id.newPlayerFName).text.toString()
        val lnameInput = findViewById<TextView>(R.id.newPlayerLName).text.toString()
        val teamInput = findViewById<AutoCompleteTextView>(R.id.newPlayerTeam)
        val dateSelect = findViewById<CalendarView>(R.id.newPlayerBorn)
        val addButton = findViewById<Button>(R.id.newPlayerAddBtn)

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        teamInput.setAdapter(adapter)

        // For storing date to send to server
        var date = LocalDateTime.now().toString()

        teamInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getTeams().map { it.TeamName }

                    adapter.clear()
                    adapter.addAll(teams)
                    adapter.notifyDataSetChanged()

                    if (teams.isNotEmpty() && !teamInput.isPopupShowing) {
                        teamInput.showDropDown()
                    }
                }
            }
        })

        dateSelect.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$year-${month + 1}-$dayOfMonth"
        }

        addButton.setOnClickListener {
            val team = teamInput.text.toString()

            if (fnameInput == "" || lnameInput == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val newPlayerString = buildJsonObject {
                        put("FName", fnameInput)
                        put("LName", lnameInput)
                        put("Birth", date)
                        put("Team", team)
                    }

                    val resp = RetrofitClient.api.postAddPlayer(newPlayerString, "Bearer ${userData.jwtTokenFlow.first()}")
                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.added_successfully,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@NewPlayerActivity, HomeActivity::class.java)
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
        }
    }
}