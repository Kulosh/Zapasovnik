package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.toString

class EditMatchActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_match_layout)

        userData = UserData(this)

        val id = intent.getIntExtra("matchId", -1)
        val team1Input = findViewById<AutoCompleteTextView>(R.id.newMatchTeam1)
        val team2Input = findViewById<AutoCompleteTextView>(R.id.newMatchTeam2)
        val leagueInput = findViewById<AutoCompleteTextView>(R.id.newMatchLeague)
        val dateSelect = findViewById<CalendarView>(R.id.newMatchDate)
        val addButton = findViewById<Button>(R.id.newMatchAddBtn)

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        team1Input.setAdapter(adapter)
        team2Input.setAdapter(adapter)
        leagueInput.setAdapter(adapter)

        // For storing date to send to server
        var date = LocalDateTime.now().toString()

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first())
                put("entityId", id)
            }
            val match = RetrofitClient.api.postMatchDetail(user)

            team1Input.setText(match.body()!!.Team1)
            team2Input.setText(match.body()!!.Team2)
            leagueInput.setText(match.body()!!.League)

            // Set date from DB
            val rawDate = match.body()!!.Date

            val normalizedDate = rawDate
                .replace('\u202F', ' ')   // narrow no-break space
                .replace('\u00A0', ' ')   // no-break space
                .replace(Regex("\\s+"), " ")
                .trim()

            val formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a", Locale.US)

            normalizedDate.let {
                val ldt = LocalDateTime.parse(it, formatter)
                val millis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                dateSelect.date = millis
            }
        }

        team1Input.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getTeams().map { it.TeamName }

                    adapter.clear()
                    adapter.addAll(teams)
                    adapter.notifyDataSetChanged()

                    if (teams.isNotEmpty() && !team1Input.isPopupShowing) {
                        team1Input.showDropDown()
                    }
                }
            }
        })

        team2Input.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getTeams().map { it.TeamName }

                    adapter.clear()
                    adapter.addAll(teams)
                    adapter.notifyDataSetChanged()

                    if (teams.isNotEmpty() && !team2Input.isPopupShowing) {
                        team2Input.showDropDown()
                    }
                }
            }
        })

        leagueInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    val teams = RetrofitClient.api.getLeagues().map { it.LeagueName }

                    adapter.clear()
                    adapter.addAll(teams)
                    adapter.notifyDataSetChanged()

                    if (teams.isNotEmpty() && !leagueInput.isPopupShowing) {
                        leagueInput.showDropDown()
                    }
                }
            }
        })

        dateSelect.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$year-${month + 1}-$dayOfMonth"
        }

        addButton.setOnClickListener {
            if (team1Input.text.toString() == "" || team2Input.text.toString() == "" || leagueInput.text.toString() == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.fill_all_fields,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val newMatchString = buildJsonObject {
                        put("Team1", team1Input.text.toString())
                        put("Team2", team2Input.text.toString())
                        put("League", leagueInput.text.toString())
                        put("Date", date)
                    }

                    val resp = RetrofitClient.api.patchEditMatch(id,newMatchString, "Bearer ${userData.jwtTokenFlow.first()}")

                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.updated_successfully,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@EditMatchActivity, HomeActivity::class.java)
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