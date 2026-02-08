package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ProfileActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.profile_layout)

        userData = UserData(this)

        val usernameText = findViewById<TextView>(R.id.profileUsername)
        val emailText = findViewById<TextView>(R.id.profileEmail)
        val changPasswordBtn = findViewById<Button>(R.id.profileChangePassword)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val favPlayersBtn = findViewById<Button>(R.id.favPlayersBtn)

        changPasswordBtn.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
//            Log.d("username profile", username)
            startActivity(intent)
        }

        logoutBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            lifecycleScope.launch {
                userData.storeUser("", "", "", "false")
                startActivity(intent)
            }
        }

        favPlayersBtn.setOnClickListener {
            val intent = Intent(this, FavPlayersActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val username = userData.usernameFlow.first()
            val email = userData.emailFlow.first()

            val usernameJson = buildJsonObject {
                put("username", username)
            }

//            Log.d("USER ID", userData.userIdFlow.first())
//            Log.d("Username", username)
//            Log.d("Email", email)

            usernameText.text = username
            emailText.text = email.replace("\"", "")
        }


    }
}