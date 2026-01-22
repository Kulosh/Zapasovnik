package com.example.zapasovnik.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch

class HomeMatchesModel : ViewModel() {
    val matchList = mutableListOf<Match>()

    fun loadMatches() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getTeamMatches()
                matchList.clear()
                matchList.addAll(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}