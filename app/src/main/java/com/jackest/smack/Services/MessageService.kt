package com.jackest.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jackest.smack.Controller.App
import com.jackest.smack.Model.Channel
import com.jackest.smack.Model.Message
import com.jackest.smack.Utilities.URL_GET_CHANNELS
import com.jackest.smack.Utilities.URL_MESSAGES
import org.json.*


object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannel( complete: (Boolean) -> Unit){
        //very important to use array rather than object
        val channelsReq = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener {    response->
            try {
                for (x in 0 until response.length()){
                    val channel = response.getJSONObject(x)
                    val name  = channel.getString("name")
                    val des  = channel.getString("description")
                    val id = channel.getString("_id")

                    val newChannel = Channel(name, des,id)
                    this. channels.add(newChannel)
                }
            }catch(e: JSONException){
                Log.d("ERROR", "EXC:"+e.localizedMessage)
            }
        }, Response.ErrorListener {error ->
            Log.d("ERROR", "Could not retrieve")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=uft-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharedPreferences.authToken}")
                return headers
            }
        }
        App.sharedPreferences.requestQueue.add(channelsReq)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit){
        val url = "$URL_MESSAGES$channelId"
        val messagesReq = object :JsonArrayRequest(Method.GET, url, null, Response.Listener { response->
            clearMessage()
            try{
                for (x in 0 until response.length()){
                    val message  = response.getJSONObject(x)
                    val messageBody = message.getString("messageBody")
                    val channelId = message.getString("channelId")
                    val id = message.getString("_id")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userColor = message.getString("userAvatarColor")
                    val time = message.getString("timeStamp")

                    val newMessage = Message(messageBody, userName, channelId, userAvatar, userColor, id, time)
                    this.messages.add(newMessage)
                }
            }catch(e:JSONException){
                Log.d("ERROR", "EXC:"+e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not receive messages")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharedPreferences.authToken}")
                return headers
            }
        }
        App.sharedPreferences.requestQueue.add(messagesReq)
    }
    fun clearMessage(){
        messages.clear()
    }
    fun clearChannel(){
        channels.clear()
    }
}