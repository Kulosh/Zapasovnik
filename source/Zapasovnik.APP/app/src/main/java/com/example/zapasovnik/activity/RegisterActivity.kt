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
//            Log.d("CLICK", "CLICK")
            if (
                email.text.toString() != "" &&
                username.text.toString() != "" &&
                pwd.text.toString() != ""
                ) {
                if (pwd.text.toString() == pwdRe.text.toString()) {
                    val regString = buildJsonObject {
                        put("username", username.text.toString())
                        put("userEmail", email.text.toString())
                        put("userPassword", pwd.text.toString())
                    }

                    lifecycleScope.launch {
                        val resp = RetrofitClient.api.postRegister(regString)
                        val user = resp.body()!!.user
                        if (resp.isSuccessful){
                            userData.storeUser(
                                userId = user.userId,
                                username = username.text.toString(),
                                email = email.text.toString(),
                                loggedIn = user.success.toString())
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "${R.string.login_failed}: ${resp.code()}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.new_not_same,
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