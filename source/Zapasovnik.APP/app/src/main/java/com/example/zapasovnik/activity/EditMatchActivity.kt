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
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.collections.addAll
import kotlin.text.clear
import kotlin.toString

class EditMatchActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_match_layout)

        userData = UserData(this)
        val id = intent.getIntExtra("matchId", -1)

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

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first().toInt())
                put("entityId", id)
            }
            val match = RetrofitClient.api.postMatchDetail(user)

            team1.setText(match.body()?.Team1)
            team2.setText(match.body()?.Team2)
            league.setText(match.body()?.League)

            val oldDate = match.body()?.Date
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val ldt = LocalDateTime.parse(oldDate, formatter)
            val millis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            dateSel.date = millis
        }

        team1.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getTeams().map { it.TeamName }

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
                    val teams = RetrofitClient.api.getLeagues().map { it.LeagueName }

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

                    val resp = RetrofitClient.api.patchEditMatch(id,newMatchString)
                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.new_match_success, //TODO: Added > Updated
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