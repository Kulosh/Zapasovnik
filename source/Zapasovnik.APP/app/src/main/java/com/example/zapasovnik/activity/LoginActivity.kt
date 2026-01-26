package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.R
import com.example.zapasovnik.model.User
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_layout)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val userName: String = findViewById<EditText>(R.id.usernameLogin).text.toString()
            val password: String = findViewById<EditText>(R.id.passwordLogin).text.toString()
            val intent = Intent(this, HomeActivity::class.java)

            if (userName != "" && password != "") {
                lifecycleScope.launch {
                    val user = User(username =  userName, password = password)
                    val response: Boolean = RetrofitClient.api.postLogin(user)

                    if (response) {
                        startActivity(intent)
                    } else {
                        // TODO: change text to values from strings.xml
                        Toast.makeText(applicationContext, "Wrong credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Invalid login", Toast.LENGTH_SHORT).show()
            }
        }
    }
}