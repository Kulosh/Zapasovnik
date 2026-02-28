package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.model.PlayerDetail
import com.example.zapasovnik.model.TeamDetail
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import retrofit2.Response

class LeagueDetailActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.league_detail_layout)

//        val isFav = intent.getBooleanExtra("isFav", false)
        val leagueId = intent.getIntExtra("id", -1)
        val name = findViewById<TextView>(R.id.leagueDetailName)
        val delTeamBtn = findViewById<Button>(R.id.deleteLeagueBtn)
        userData = UserData(this)
        val edit = findViewById<Button>(R.id.editLeagueBtn)

        lifecycleScope.launch {
            val league = RetrofitClient.api.getLeagueDetail(leagueId)
//            val loggedIn = userData.loggedInFlow.first().toBoolean()

            name.text = league.body()?.LeagueName

//            if (loggedIn)
//            {
//                if (isFav!!) {
//                    favBtn.visibility = Button.GONE
//                } else {
//                    unfavBtn.visibility = Button.GONE
//                }
//            } else {
//                favBtn.visibility = Button.GONE
//                unfavBtn.visibility = Button.GONE
//            }
        }

        delTeamBtn.setOnClickListener {
            lifecycleScope.launch {
                val resp = RetrofitClient.api.deleteLeague(leagueId)

                if (resp.isSuccessful && resp.body() == true) {
                    val intent = Intent(this@LeagueDetailActivity, HomeActivity::class.java)
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

        edit.setOnClickListener {
            val intent = Intent(this, EditLeagueActivity::class.java)
            intent.putExtra("leagueId", leagueId)
            startActivity(intent)
        }
    }
}