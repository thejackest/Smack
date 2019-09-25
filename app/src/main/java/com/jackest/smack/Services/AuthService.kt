package com.jackest.smack.Services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.json.JSONObject
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jackest.smack.Utilities.*
import org.json.JSONException
import kotlin.collections.HashMap

object AuthService {

    var isLoggedin = false
    var userEmail = ""
    var authToken = ""

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

            try{
                userEmail = response.getString("user")//what the user actually inputs
                authToken = response.getString("token")//value for the input
                isLoggedin = true
                complete(true)
            }catch(e: JSONException){
                Log.d("JSON","EXE:" +e.localizedMessage)
                complete(false)
            }

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
    fun createUser(context:Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit){
        //json body objects
        val jsonBody  = JSONObject()
        jsonBody.put("name",name)
        jsonBody.put("email",email)
        jsonBody.put("avatarName",avatarName)
        jsonBody.put("avatarColor",avatarColor)
        //need to convert to byte array
        val requestBody = jsonBody.toString()

        val createReq = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->
            try {
                UserDataService.name = response.getString("name")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.id = response.getString("id")
                UserDataService.email = response.getString("email")
            }catch (e:JSONException){
                Log.d("JSON","EXC"+ e.localizedMessage)
            }
        }, Response.ErrorListener{  error->
            Log.d("ERROR","Could not register user : $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset = utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers =  HashMap<String,String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(createReq)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit){
        val finduUserReq = object : JsonObjectRequest(Method.GET,"$URL_GET_USER$userEmail", null, Response.Listener {response->
            try{
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")

                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            }catch(e:JSONException){
                Log.d("JSON","EXC: "+ e.localizedMessage)
            }
        }, Response.ErrorListener {error ->
            Log.d("ERROR","Could not find user.")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset = utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization","Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(finduUserReq)
    }
}