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
import android.widget.Spinner
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
import java.time.format.DateTimeFormatter
import java.util.Date

class NewPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_player_layout)

        val teamSel = findViewById<AutoCompleteTextView>(R.id.newPlayerTeam)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        teamSel.setAdapter(adapter)
        val addBtn = findViewById<Button>(R.id.newPlayerAddBtn)
        var birth = LocalDateTime.now().toString()
        val date = findViewById<CalendarView>(R.id.newPlayerBorn)

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
            val fname = findViewById<TextView>(R.id.newPlayerFName).text.toString()
            val lname = findViewById<TextView>(R.id.newPlayerLName).text.toString()
            val team = teamSel.text.toString()

//            Log.d("Birth", birth)
//            Log.d("Team", team)

            if (fname == "" || lname == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.new_player_enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, HomeActivity::class.java)

                lifecycleScope.launch {
                    val newPlayerString = buildJsonObject {
                        put("FName", fname)
                        put("LName", lname)
                        put("Birth", birth)
                        put("Team", team)
                    }

                    Log.d("String", newPlayerString.toString())

                    val resp = RetrofitClient.api.postAddPlayer(newPlayerString)
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