package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.JwtDecoder
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class LoginActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_layout)

        userData = UserData(this)
        val intent = Intent(this, HomeActivity::class.java)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val regBtn = findViewById<Button>(R.id.loginRegBtn)

        regBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val username: String = findViewById<EditText>(R.id.usernameLogin).text.toString()
            val password: String = findViewById<EditText>(R.id.passwordLogin).text.toString()
            val loginString = buildJsonObject {
                put("userName", username)
                put("userPassword", password)
            }

            if (username != "" && password != "") {
                lifecycleScope.launch {
                    try {
                        val resp = RetrofitClient.api.postUser(loginString).string()

//                        Log.d("Token", resp)

                        val jwtToken = JwtDecoder.decodeJwtWithoutVerification(resp)

//                        Log.d("PAYLOAD", jwtToken.payload.toString())

                        if (jwtToken.payload.has("uid")) {
                            val email = jwtToken.payload.getString("email")
                            val id = jwtToken.payload.getString("uid")
                            val expire = jwtToken.payload.getInt("exp")
                            val admin = jwtToken.payload.getBoolean("role")

//                            Log.d("Token", resp)

                            if (id != "-1") {
                                userData.storeUser(id.toInt(), username, email, resp, expire, admin)
                                startActivity(intent)
                            }
                            else Toast.makeText(applicationContext, R.string.invalid_credentials, Toast.LENGTH_SHORT).show()
                        } else {
//                            val err = resp.errorBody()?.string()
//                            Log.d("LoginString", "$loginString")
//                            Log.e("API", "HTTP ${resp.code()} error=$err")
                            Toast.makeText(applicationContext, "${R.string.login_failed}", Toast.LENGTH_SHORT).show()
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