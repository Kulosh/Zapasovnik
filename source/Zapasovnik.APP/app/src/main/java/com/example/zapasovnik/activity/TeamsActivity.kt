package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.FavTeamsTableAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TeamsActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.teams_list_layout)

        userData = UserData(this)

        val recyclerView = findViewById<RecyclerView>(R.id.teamsTableView)
        val addButton = findViewById<Button>(R.id.teamAddBtn)

        lifecycleScope.launch {
            val teams = RetrofitClient.api.getTeams()
            val isAdmin = userData.adminFlow.first()

            if (!isAdmin) addButton.visibility = Button.GONE

            recyclerView.adapter = FavTeamsTableAdapter(teams) { team ->
                val intent = Intent(this@TeamsActivity, TeamDetailActivity::class.java)
                intent.putExtra("id", team.TeamId)
                startActivity(intent)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            val intent = Intent(this, NewTeamActivity::class.java)
            startActivity(intent)
        }
    }
}