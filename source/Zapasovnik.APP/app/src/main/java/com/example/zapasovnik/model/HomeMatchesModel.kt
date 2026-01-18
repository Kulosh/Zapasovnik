package com.example.zapasovnik.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zapasovnik.Match
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch

class HomeMatchesModel : ViewModel() {
    val matchList = mutableListOf<Match>()

    fun loadMatches() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getMatches()
                matchList.clear()
                matchList.addAll(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}