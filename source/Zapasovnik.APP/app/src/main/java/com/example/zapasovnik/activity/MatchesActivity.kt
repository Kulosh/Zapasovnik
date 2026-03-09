package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.HomeMatchTableAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MatchesActivity : ComponentActivity() {

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.matches_list_layout)

        userData = UserData(this)

        val recyclerView = findViewById<RecyclerView>(R.id.matchesTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val addBtn = findViewById<Button>(R.id.addMatchBtn)

        addBtn.setOnClickListener {
            val intent = Intent(this, NewMatchActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val matches = RetrofitClient.api.getTeamMatches()
//            Log.d("Matches", "${matches}")
            val isAdmin = userData.adminFlow.first()

            if (!isAdmin) addBtn.visibility = Button.GONE

            recyclerView.adapter = HomeMatchTableAdapter(matches) { match ->
                val intent = Intent(this@MatchesActivity, MatchDetailActivity::class.java)
                intent.putExtra("id", match.Id)
//                Log.d("Match CLICK", match.toString())
                startActivity(intent)
            }
        }
    }
}