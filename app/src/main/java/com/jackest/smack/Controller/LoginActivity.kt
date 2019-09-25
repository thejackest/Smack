package com.jackest.smack.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jackest.smack.R
import com.jackest.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun loginLoginBtnClicked(view: View){
        val email = loginemailText.text.toString()
        val password = loginPasswordText.text.toString()

        AuthService.loginUser(this,email, password){loginSuccess->
            if (loginSuccess){
                AuthService.findUserByEmail(this){findSuccess ->
                    if(findSuccess){
                        finish()
                    }
                }
            }
        }
    }
    fun loginCreateClicked(view: View){
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }
}
