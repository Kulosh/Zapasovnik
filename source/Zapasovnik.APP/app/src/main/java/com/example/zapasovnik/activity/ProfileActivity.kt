package com.example.zapasovnik.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.serialization.json.put
import kotlinx.serialization.json.buildJsonObject

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.profile_layout)

        val username = intent.getStringExtra("username").toString()
        val usernameJson = buildJsonObject {
            put("username", username)
        }
        val usernameText = findViewById<TextView>(R.id.profileUsername)
        val emailText = findViewById<TextView>(R.id.profileEmail)

        lifecycleScope.launch {
            val resp = RetrofitClient.api.getUser(usernameJson)
            usernameText.text = username
            emailText.text = resp.getValue("email").toString().replace("\"", "")
        }
    }
}