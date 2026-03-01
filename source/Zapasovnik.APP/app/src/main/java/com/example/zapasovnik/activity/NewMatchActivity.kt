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
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.collections.addAll
import kotlin.text.clear
import kotlin.toString

class NewMatchActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_match_layout)

        userData = UserData(this)

        val team1 = findViewById<AutoCompleteTextView>(R.id.newMatchTeam1)
        val team2 = findViewById<AutoCompleteTextView>(R.id.newMatchTeam2)
        val league = findViewById<AutoCompleteTextView>(R.id.newMatchLeague)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        team1.setAdapter(adapter)
        team2.setAdapter(adapter)
        league.setAdapter(adapter)

        var date = LocalDateTime.now().toString()
        val dateSel = findViewById<CalendarView>(R.id.newMatchDate)
        val addBtn = findViewById<Button>(R.id.newMatchAddBtn)

        team1.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getTeams().map { it.TeamName }
//                    Log.d("Teams", "$teams")

                    adapter.clear()
                    adapter.addAll(teams)
                    adapter.notifyDataSetChanged()

                    if (teams.isNotEmpty() && !team1.isPopupShowing) {
                        team1.showDropDown()
                    }
                }
            }
        })

        team2.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getTeams().map { it.TeamName }

                    adapter.clear()
                    adapter.addAll(teams)
                    adapter.notifyDataSetChanged()

                    if (teams.isNotEmpty() && !team2.isPopupShowing) {
                        team2.showDropDown()
                    }
                }
            }
        })

        league.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getLeagues("Bearer ${userData.jwtTokenFlow.first()}").map { it.LeagueName }

                    adapter.clear()
                    adapter.addAll(teams)
                    adapter.notifyDataSetChanged()

                    if (teams.isNotEmpty() && !league.isPopupShowing) {
                        league.showDropDown()
                    }
                }
            }
        })

        dateSel.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date = "$year-${month + 1}-$dayOfMonth"
        }

        addBtn.setOnClickListener {
            if (team1.text.toString() == "" || team2.text.toString() == "" || league.text.toString() == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.fill_all_fields,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, HomeActivity::class.java)

                lifecycleScope.launch {
                    val newMatchString = buildJsonObject {
                        put("Team1", team1.text.toString())
                        put("Team2", team2.text.toString())
                        put("League", league.text.toString())
                        put("Date", date)
                    }

                    val resp = RetrofitClient.api.postAddMatch(newMatchString, "Bearer ${userData.jwtTokenFlow.first()}")
                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.new_match_success,
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