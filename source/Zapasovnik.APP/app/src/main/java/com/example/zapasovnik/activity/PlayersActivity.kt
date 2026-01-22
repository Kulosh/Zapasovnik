package com.example.zapasovnik.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import kotlinx.coroutines.launch

class PlayersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.players_list_layout)

//        lifecycleScope.launch {
//            try {
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }
}