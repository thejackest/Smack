package com.jackest.smack.Services

import android.content.Context
import android.util.Log
import com.jackest.smack.Utilities.URL_REGISTER
import org.json.JSONObject
import java.lang.reflect.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jackest.smack.Utilities.URL_LOGIN

object AuthService {
    fun regisiterUser(context: Context, email:String, password:String, complete:(Boolean)->Unit){

        //json body objects
        val jsonBody  = JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        //need to convert to byte array
        val requestBody = jsonBody.toString()

        //web request
        //method type
        val registerReq = object: StringRequest(Method.POST, URL_REGISTER, Response.Listener{response ->
            println(response)  //response
            complete(true)
        },Response.ErrorListener{ error->    //error response
            Log.d("ERROR","Could not register user : $error")
            complete(false)
        }){
            //content type body
            override fun getBodyContentType(): String {
                return "application/json:charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

        }
        //add to queue(volley)
        Volley.newRequestQueue(context).add(registerReq)
    }
    fun loginUser(context: Context, email:String, password: String, complete: (Boolean) -> Unit){
        //json body objects
        val jsonBody  = JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        //need to convert to byte array
        val requestBody = jsonBody.toString()

        val loginReq = object :JsonObjectRequest(Method.POST, URL_LOGIN,null, Response.Listener {response ->
            println(response)  //response
            //complete(true)
            val authToken = response.getString("token")
        },Response.ErrorListener {error->    //error response
            Log.d("ERROR","Could not register user : $error")
            complete(false)
        }){
            //content body type
            override fun getBodyContentType(): String {
                return "application/json:charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(loginReq)
    }
}