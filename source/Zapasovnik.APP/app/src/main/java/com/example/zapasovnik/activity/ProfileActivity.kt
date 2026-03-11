package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.profile_layout)

        userData = UserData(this)

        val usernameView = findViewById<TextView>(R.id.profileUsername)
        val emailView = findViewById<TextView>(R.id.profileEmail)
        val changPasswordButton = findViewById<Button>(R.id.profileChangePasswordBtn)
        val leaguesButton = findViewById<Button>(R.id.profileLeaguesBtn)
        val favPlayersButton = findViewById<Button>(R.id.profileFavPlayersBtn)
        val favMatchesButton = findViewById<Button>(R.id.profileFavMatchesBtn)
        val favTeamsButton = findViewById<Button>(R.id.profileFavTeamsBtn)
        val logoutButton = findViewById<Button>(R.id.profileLogoutBtn)

        lifecycleScope.launch {
            val username = userData.usernameFlow.first()
            val email = userData.emailFlow.first()

            usernameView.text = username
            emailView.text = email

            val isAdmin = userData.adminFlow.first()
            if (!isAdmin) {
                leaguesButton.visibility = Button.GONE
            } else {
                favPlayersButton.visibility = Button.GONE
                favMatchesButton.visibility = Button.GONE
                favTeamsButton.visibility = Button.GONE
            }

        }

        changPasswordButton.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        leaguesButton.setOnClickListener {
            val intent = Intent(this, LeaguesActivity::class.java)
            startActivity(intent)
        }

        favPlayersButton.setOnClickListener {
            val intent = Intent(this, FavPlayersActivity::class.java)
            startActivity(intent)
        }

        favMatchesButton.setOnClickListener {
            val intent = Intent(this, FavMatchesActivity::class.java)
            startActivity(intent)
        }

        favTeamsButton.setOnClickListener {
            val intent = Intent(this, FavTeamsActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            lifecycleScope.launch {
                userData.storeUser(-1, "", "", "", 0, false)
                startActivity(intent)
            }
        }
    }
}