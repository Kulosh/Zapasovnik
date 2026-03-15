package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
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

class EditTeamActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_team_layout)

        userData = UserData(this)

        val id = intent.getIntExtra("teamId", -1)
        val nameInput = findViewById<TextView>(R.id.newTeamName)
        val dateSelect = findViewById<CalendarView>(R.id.newTeamEst)
        val addButton = findViewById<Button>(R.id.newTeamAddBtn)

        var date = LocalDateTime.now().toString()

        lifecycleScope.launch {
            val user = buildJsonObject {
                put("userId", userData.userIdFlow.first())
                put("entityId", id)
            }
            val team = RetrofitClient.api.postTeamDetail(user)

            nameInput.text = team.body()!!.Name

            // Set date from DB
            val rawDate = team.body()!!.Established

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

        dateSelect.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$year-${month + 1}-$dayOfMonth"
        }

        addButton.setOnClickListener {
            if (nameInput.text == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val newTeamString = buildJsonObject {
                        put("TeamName", nameInput.text.toString())
                        put("TeamEstablished", date)
                    }

                    val resp = RetrofitClient.api.patchEditTeam(id,newTeamString, "Bearer ${userData.jwtTokenFlow.first()}")
                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.updated_successfully,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@EditTeamActivity, HomeActivity::class.java)
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