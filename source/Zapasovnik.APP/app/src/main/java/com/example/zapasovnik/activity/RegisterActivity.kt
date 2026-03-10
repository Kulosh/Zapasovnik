package com.example.zapasovnik.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zapasovnik.model.JwtDecoder
import com.example.zapasovnik.R
import com.example.zapasovnik.model.UserData
import com.example.zapasovnik.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RegisterActivity : ComponentActivity() {
    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_layout)

        userData = UserData(this)

        val usernameInput = findViewById<EditText>(R.id.regUsername)
        val emailInput = findViewById<EditText>(R.id.regEmail)
        val pwd1Input = findViewById<EditText>(R.id.regPwd)
        val pwd2Input = findViewById<EditText>(R.id.regPwdRe)
        val registerButton = findViewById<Button>(R.id.regBtnConfirm)

        registerButton.setOnClickListener {
            if (
                emailInput.text.toString() != "" &&
                usernameInput.text.toString() != "" &&
                pwd1Input.text.toString() != ""
                ) {
                if (pwd1Input.text.toString() == pwd2Input.text.toString()) {
                    val regString = buildJsonObject {
                        put("username", usernameInput.text.toString())
                        put("userEmail", emailInput.text.toString())
                        put("userPassword", pwd1Input.text.toString())
                    }

                    lifecycleScope.launch {
                        val resp = RetrofitClient.api.postRegister(regString).string()
                        val jwtToken = JwtDecoder.decodeJwtWithoutVerification(resp)
                        val email = jwtToken.payload.getString("email")
                        val id = jwtToken.payload.getString("uid").toInt()
                        val expire = jwtToken.payload.getInt("exp")
                        val admin = jwtToken.payload.getBoolean("role")


                        if (id != -1){
                            userData.storeUser(
                                userId = id,
                                username = usernameInput.text.toString(),
                                email = email,
                                jwtToken = resp,
                                jwtExpire = expire,
                                admin = admin
                            )

                            val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "${R.string.login_failed}",
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.passwords_not_same,
                        Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.fill_all_fields,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}