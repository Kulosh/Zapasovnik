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
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_layout)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val username: String = findViewById<EditText>(R.id.usernameLogin).text.toString()
            val password: String = findViewById<EditText>(R.id.passwordLogin).text.toString()
            val loginString = buildJsonObject {
                put("userName", username)
                put("userPassword", password)
            }
            val intent = Intent(this, HomeActivity::class.java)

            if (username != "" && password != "") {
                lifecycleScope.launch {
                    try {
                        val resp = RetrofitClient.api.postLogin(loginString)

                        if (resp.isSuccessful) {
                            val ok = resp.body() == true
                            if (ok) startActivity(intent)
                            else Toast.makeText(applicationContext, R.string.invalid_credentials, Toast.LENGTH_SHORT).show()
                        } else {
//                            val err = resp.errorBody()?.string()
//                            Log.d("LoginString", "$loginString")
//                            Log.e("API", "HTTP ${resp.code()} error=$err")
                            val msg = R.string.login_failed.toString() + ": " + resp.code()
                            Toast.makeText(applicationContext, "($msg)", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
//                        Log.d("LoginString", "$loginString")
//                        Log.e("API", "Network/serialization error", e)
                        Toast.makeText(applicationContext, R.string.network_error, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, R.string.invalid_login , Toast.LENGTH_SHORT).show()
            }
        }
    }
}