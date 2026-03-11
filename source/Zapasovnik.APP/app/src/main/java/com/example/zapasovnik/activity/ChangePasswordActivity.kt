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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ChangePasswordActivity : ComponentActivity() {

    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.change_password_layout)

        userData = UserData(this)

        val oldPwdInput = findViewById<EditText>(R.id.changePwdOld)
        val newPwd1Input = findViewById<EditText>(R.id.changePwdNew)
        val newPwd2Input = findViewById<EditText>(R.id.changePwdNewRe)
        val confirmButton = findViewById<Button>(R.id.changePwdConfirm)

        confirmButton.setOnClickListener {
            if (newPwd1Input.text.toString() == newPwd2Input.text.toString()) {
                lifecycleScope.launch {
                    val userId = userData.userIdFlow.first()

                    val pwdString = buildJsonObject {
                        put("UserId", userId)
                        put("Old", oldPwdInput.text.toString())
                        put("New", newPwd1Input.text.toString())
                    }
                    val resp = RetrofitClient.api.postChangePassword(pwdString, "Bearer ${userData.jwtTokenFlow.first()}")
                    val success = resp.body()

                    if (success == true) {
                        val intent = Intent(this@ChangePasswordActivity, ProfileActivity::class.java)
                        startActivity(intent)
                    } else Toast.makeText(
                        applicationContext,
                        R.string.old_password_incorrect,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else Toast.makeText(
                applicationContext,
                R.string.passwords_not_same,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}