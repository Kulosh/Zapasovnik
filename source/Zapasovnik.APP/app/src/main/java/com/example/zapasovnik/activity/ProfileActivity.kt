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

//        val username = intent.getStringExtra("username").toString()
        val username = userData.usernameFlow.toString()

        val usernameJson = buildJsonObject {
            put("username", username)
        }
        val usernameText = findViewById<TextView>(R.id.profileUsername)
        val emailText = findViewById<TextView>(R.id.profileEmail)
        val changPasswordBtn = findViewById<Button>(R.id.profileChangePassword)

        changPasswordBtn.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
//            intent.putExtra("usernamePwd", username)
//            Log.d("username profile", username)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val resp = RetrofitClient.api.getUser(usernameJson)
            val email = resp.getValue("email").toString().replace("\"", "")
            userData.storeUser(username, email, true)

            Log.e("Username", username)
            Log.e("Email", email)


            usernameText.text = username
            emailText.text = email
        }


    }
}