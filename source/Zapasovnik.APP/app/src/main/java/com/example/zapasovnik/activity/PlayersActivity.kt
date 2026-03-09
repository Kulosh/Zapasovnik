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
import com.example.zapasovnik.viewModel.PlayersTableAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlayersActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.players_list_layout)

        userData = UserData(this)

        val recyclerView = findViewById<RecyclerView>(R.id.playersTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val addPlayer = findViewById<Button>(R.id.addPlayerBtn)

        addPlayer.setOnClickListener {
            val intent = Intent(this, NewPlayerActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val players = RetrofitClient.api.getPlayers()
            val isAdmin = userData.adminFlow.first()

            if (!isAdmin) addPlayer.visibility = Button.GONE

            recyclerView.adapter = PlayersTableAdapter(players) { player ->
                val intent = Intent(this@PlayersActivity, PlayerDetailActivity::class.java)
                intent.putExtra("id", player.Id)
                startActivity(intent)
            }
        }
    }
}