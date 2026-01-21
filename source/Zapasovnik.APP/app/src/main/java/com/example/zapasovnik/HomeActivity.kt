package com.example.zapasovnik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapasovnik.model.Match
import com.example.zapasovnik.viewModel.HomeMatchTableAdapter
import com.example.zapasovnik.ui.theme.ZapasovnikTheme
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.homeMatchTableView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
//                var matches: List<Match> = RetrofitClient.api.getMatches()
                var matches = ArrayList<Match>();

                matches.add(Match("sparta", "Tmrw", "Slavia"))
                matches.add(Match("Boh", "Tmrw", "Ban"))
                matches.add(Match("Plz", "Tmrw", "ManC"))
                matches.add(Match("Tot", "Tmrw", "ManU"))


//                fillTable(matches)
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
//    val targetTable =
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