package com.example.zapasovnik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zapasovnik.ui.theme.ZapasovnikTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZapasovnikTheme {
                Scaffold(modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.statusBars)
                ) { innerPadding ->
                    ZapasovnikHomepage(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ZapasovnikHomepage(modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Zapasovnik",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(20.dp)
        )

        Button(onClick = {}, modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Matches"
            )
        }

        Button(onClick = {}, modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Teams"
            )
        }

        Button(onClick = {}, modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Players"
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun ZapasovnikPreview(){
    Scaffold(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background)
        .windowInsetsPadding(WindowInsets.statusBars)
    ) { innerPadding ->
        ZapasovnikHomepage(Modifier
            .fillMaxSize()
            .padding(innerPadding)
        )
    }
}