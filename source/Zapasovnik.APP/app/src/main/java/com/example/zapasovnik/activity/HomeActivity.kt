package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.HomeMatchTableAdapter
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.homeMatchTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val playersClick = findViewById<Button>(R.id.playerBtn)
        playersClick.setOnClickListener {
            val intent = Intent(this, PlayersActivity::class.java)
            startActivity(intent)
        }

        val loginClick = findViewById<ImageView>(R.id.loginIcon)
        loginClick.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            try {
                var TeamMatches: List<Match> = RetrofitClient.api.getTeamMatches()
//                var matches = ArrayList<Match>();

//                matches.add(Match("sparta", "Tmrw", "Slavia"))
//                matches.add(Match("Boh", "Tmrw", "Ban"))
//                matches.add(Match("Plz", "Tmrw", "ManC"))
//                matches.add(Match("Tot", "Tmrw", "ManU"))


//                fillTable(matches)
                recyclerView.adapter = HomeMatchTableAdapter(TeamMatches)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

private fun fillTable(matches: List<Match>) {
//    val targetTable =
}