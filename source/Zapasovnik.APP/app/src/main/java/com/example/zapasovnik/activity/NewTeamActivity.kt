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
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.time.LocalDateTime

class NewTeamActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_team_layout)

        userData = UserData(this)

        val nameInput = findViewById<TextView>(R.id.newTeamName).text.toString()
        val dateSelect = findViewById<CalendarView>(R.id.newTeamEst)
        val addButton = findViewById<Button>(R.id.newTeamAddBtn)

        var date = LocalDateTime.now().toString()

        dateSelect.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date = "$year-${month + 1}-$dayOfMonth"
        }

        addButton.setOnClickListener {
            if (nameInput == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val newTeamString = buildJsonObject {
                        put("TeamName", nameInput)
                        put("TeamEstablished", date)
                    }

                    val resp = RetrofitClient.api.postAddTeam(newTeamString, "Bearer ${userData.jwtTokenFlow.first()}")
                    if (resp.isSuccessful && resp.body().toString() == "true") {
                        Toast.makeText(
                            applicationContext,
                            R.string.added_successfully,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@NewTeamActivity, HomeActivity::class.java)
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