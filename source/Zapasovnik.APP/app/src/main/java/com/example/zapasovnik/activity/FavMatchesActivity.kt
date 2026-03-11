package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.viewModel.HomeMatchTableAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavMatchesActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fav_matches_layout)

        userData = UserData(this)

        val recyclerView = findViewById<RecyclerView>(R.id.favMatchesTableView)

        lifecycleScope.launch {
            val userId = userData.userIdFlow.first()
            val favMatches = RetrofitClient.api.postFavMatch(userId, "Bearer ${userData.jwtTokenFlow.first()}")

            recyclerView.adapter = HomeMatchTableAdapter(favMatches) { match ->
                val intent = Intent(this@FavMatchesActivity, MatchDetailActivity::class.java)
                intent.putExtra("id", match.Id)
                startActivity(intent)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}