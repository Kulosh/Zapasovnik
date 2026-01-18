package com.example.zapasovnik

import android.os.Bundle
import android.widget.TableLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.model.HomeMatchTableAdapter
import com.example.zapasovnik.network.RetrofitClient
import com.example.zapasovnik.ui.theme.ZapasovnikTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.homeMatchTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val matches: List<Match> = RetrofitClient.api.getMatches()
                fillTable(matches)
                recyclerView.adapter = HomeMatchTableAdapter(matches)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//        setContent {
//            ZapasovnikTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
    }
}

private fun fillTable(matches: List<Match>) {
//    val targetTable = findViewById<TableLayout>
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZapasovnikTheme {
        Greeting("Android")
    }
}