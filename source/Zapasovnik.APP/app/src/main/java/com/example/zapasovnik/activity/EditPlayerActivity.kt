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
        val fnameInput = findViewById<TextView>(R.id.newPlayerFName)
        val lnameInput = findViewById<TextView>(R.id.newPlayerLName)
        val teamInput = findViewById<AutoCompleteTextView>(R.id.newPlayerTeam)
        val dateSelect = findViewById<CalendarView>(R.id.newPlayerBorn)
        val addButton = findViewById<Button>(R.id.newPlayerAddBtn)

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        teamInput.setAdapter(adapter)

        // For storing date to send to server
        var date = LocalDateTime.now().toString()

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first())
                put("entityId", id)
            }
            val player = RetrofitClient.api.postPlayerDetail(user)

            fnameInput.text = player.body()!!.FName
            lnameInput.text = player.body()!!.LName

            if (player.body()!!.Team != "No team") teamInput.setText(player.body()!!.Team)

            // Set date from DB
            val rawDate = player.body()!!.Birth

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

            if (fnameInput.text == "" || lnameInput.text == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val newPlayerString = buildJsonObject {
                        put("FName", fnameInput.text.toString())
                        put("LName", lnameInput.text.toString())
                        put("Birth", date)
                        put("Team", team)
                    }

                    val resp = RetrofitClient.api.patchEditPlayer(id,newPlayerString, "Bearer ${userData.jwtTokenFlow.first()}")

                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.added_successfully,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@EditPlayerActivity, HomeActivity::class.java)
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