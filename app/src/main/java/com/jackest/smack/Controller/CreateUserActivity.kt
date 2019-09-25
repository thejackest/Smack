package com.jackest.smack.Controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jackest.smack.R
import com.jackest.smack.Services.AuthService
import com.jackest.smack.Services.UserDataService
import com.jackest.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar  = "profileDefault"
    var avatarColor = "[0.5,0.5,0.5,1]"//api for mac, between 0-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        createSpinner.visibility = View.INVISIBLE
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
        enableSpinner(true)
        val userName = createNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        if (userName.isNotEmpty()&&email.isNotEmpty()&&password.isNotEmpty()){
            AuthService.regisiterUser(this,email,password){
                    registerSuccess ->
                if (registerSuccess){
                    AuthService.loginUser(this, email,password){
                            loginSuccess ->
                        if (loginSuccess){
                            AuthService.createUser(this,userName, email, userAvatar,avatarColor){createSuccess ->
                                if(createSuccess){
                                    //println(UserDataService.avatarName)
                                    //println(UserDataService.avatarColor)
                                    //println(UserDataService.name)

                                    //tell the main activity what's going on
                                    val userDataChange  =Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    enableSpinner(false)
                                    finish()
                                }else{errorToast()}
                            }
                        }else{errorToast()}
                    }
                }else{errorToast()}
            }
        }else{
            Toast.makeText(this,"Make sure user name, email and password are filled in.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun errorToast(){
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean){
        if(enable){
            createSpinner.visibility = View.VISIBLE
        }else{
            createSpinner.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled = !enable
        createAvatarimageView.isEnabled = !enable
        generateBackgroundColorBtn.isEnabled = !enable
    }
}
