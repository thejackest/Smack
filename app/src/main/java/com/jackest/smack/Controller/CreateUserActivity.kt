package com.jackest.smack.Controller

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jackest.smack.R
import com.jackest.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar  = "profileDefault"
    var avatarColor = "[0.5,0.5,0.5,1]"//api for mac, between 0-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }
    fun generateUserAvatar(view: View){
        val random = Random()
        val color = random.nextInt(2)//0,1
        val avatar = random.nextInt(28)//0-27

        userAvatar = if (color==0){
            "light$avatar"
        }else{
            "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar,"drawable",packageName)
        createAvatarimageView.setImageResource(resourceId)
    }
    fun generateBackgroundColorClicked(view: View){
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarimageView.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble()/255
        val savedG = g.toDouble()/255
        val savedB = b.toDouble()/255
        avatarColor = "[$savedR,$savedG,$savedB,1]"//for mac

    }
    fun createUserClicked(view: View){
        val userName = createNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        AuthService.regisiterUser(this,"dhaj","sda"){
            registerSuccess ->
            if (registerSuccess){
                AuthService.loginUser(this, email,password){
                    loginSuccess ->
                    if (loginSuccess){
                        println(AuthService.authToken)
                        println(AuthService.userEmail)
                    }
                }
            }
        }
    }
}
