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
        val oldPwd = findViewById<EditText>(R.id.changePwdOld)
        val newPwd = findViewById<EditText>(R.id.changePwdNew)
        val newPwdRe = findViewById<EditText>(R.id.changePwdNewRe)
        val confirmBtn = findViewById<Button>(R.id.changeConfirm)
//        val username = intent.getStringExtra("usernamePwd")
//        Log.w("Username", "$username")


        confirmBtn.setOnClickListener {
            if (newPwd.text.toString() == newPwdRe.text.toString()) {
                val intent = Intent(this, ProfileActivity::class.java)

                lifecycleScope.launch {
                    val username = userData.usernameFlow.first()

                    val pwdString = buildJsonObject {
                        put("username", username)
                        put("old", oldPwd.text.toString())
                        put("new", newPwd.text.toString())
                    }
                    val resp = RetrofitClient.api.postChangePassword(pwdString)
                    Log.d("response", "$resp")
                    Log.d("PWD string", pwdString.toString())
                    if (resp.isSuccessful) {
                        startActivity(intent)
                    } else Toast.makeText(
                        applicationContext,
                        R.string.old_wrong,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else Toast.makeText(applicationContext, R.string.new_not_same, Toast.LENGTH_SHORT)
                .show()
        }
    }
}