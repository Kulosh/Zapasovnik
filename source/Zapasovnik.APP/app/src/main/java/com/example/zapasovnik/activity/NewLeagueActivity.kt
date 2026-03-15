package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

class NewLeagueActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_league_layout)

        userData = UserData(this)

        val nameInput = findViewById<TextView>(R.id.newLeagueName)
        val addButton = findViewById<Button>(R.id.newLeagueAddBtn)

        addButton.setOnClickListener {

            if (nameInput.text.toString() == "") {
                Toast.makeText(
                    applicationContext,
                    R.string.enter_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val league = buildJsonObject {
                        put("LeagueName", nameInput.text.toString())
                    }

                    val resp = RetrofitClient.api.postAddLeague(league, "Bearer ${userData.jwtTokenFlow.first()}")
                    if (resp.isSuccessful && resp.body()!!) {
                        Toast.makeText(
                            applicationContext,
                            R.string.added_successfully,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@NewLeagueActivity, HomeActivity::class.java)
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