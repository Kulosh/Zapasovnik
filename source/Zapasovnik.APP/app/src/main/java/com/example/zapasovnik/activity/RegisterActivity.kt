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
        val intent = Intent(this, HomeActivity::class.java)
        val regBtn = findViewById<Button>(R.id.regBtnConfirm)
        val username = findViewById<EditText>(R.id.regUsername)
        val email = findViewById<EditText>(R.id.regEmail)
        val pwd = findViewById<EditText>(R.id.regPwd)
        val pwdRe = findViewById<EditText>(R.id.regPwdRe)

        regBtn.setOnClickListener {
            if (pwd.text.toString() === pwdRe.text.toString()) {
                val regString = buildJsonObject {
                    put("username", username.text.toString())
                    put("email", email.text.toString())
                    put("password", pwd.text.toString())
                }

                lifecycleScope.launch {
                    val resp = RetrofitClient.api.postRegister(regString)
                    if (resp.isSuccessful){
                        userData.storeUser(
                            userId = resp.body()?.getValue("userId").toString(),
                            username = username.text.toString(),
                            email = email.text.toString(),
                            loggedIn = resp.body()?.getValue("success").toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "${R.string.login_failed}: ${resp.code()}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}