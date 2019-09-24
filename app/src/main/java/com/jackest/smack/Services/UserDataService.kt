package com.jackest.smack.Services

import android.graphics.Color
import java.util.*

object UserDataService {
    var id = ""
    var avatarColor = ""
    var email = ""
    var name = ""
    var avatarName =""

    fun logout(){
        id = ""
        avatarColor = ""
        email = ""
        name = ""
        avatarName =""
        AuthService.authToken = ""
        AuthService.userEmail = ""
        AuthService.isLoggedin = false
    }

    fun returnAvatarColor(components: String):Int{
        val strippedColor = components
            .replace("[","")
            .replace("]","")
            .replace(",","")
        var r = 0
        var g =0
        var b = 0
        val scanner = Scanner(strippedColor)
        if(scanner.hasNext()){
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r,g,b)
    }
}