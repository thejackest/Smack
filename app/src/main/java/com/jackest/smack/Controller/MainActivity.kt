package com.jackest.smack.Controller

import android.app.AlertDialog
import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jackest.smack.Model.Channel
import com.jackest.smack.R
import com.jackest.smack.Services.AuthService
import com.jackest.smack.Services.MessageService
import com.jackest.smack.Services.UserDataService
import com.jackest.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import com.jackest.smack.Utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

//    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var channelAdapter : ArrayAdapter<Channel>
    val socket = IO.socket(SOCKET_URL)
    var selectedChannel :Channel? = null

    private fun setupAdapters(){
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter //then call the adapter in toggles
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle  = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setupAdapters()

        channel_list.setOnItemClickListener{_, _, i , _->
            selectedChannel = MessageService.channels[i]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()
        }

        hideKeyboard()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
            BROADCAST_USER_DATA_CHANGE))

        socket.connect()
        socket.on("channelCreated",onNewChannel)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show() //similar to toast, pop up message

        if(App.sharedPreferences.isLoggedIn){
            AuthService.findUserByEmail(this){}
        }
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
            BROADCAST_USER_DATA_CHANGE))

        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        socket.disconnect()
        super.onDestroy()
    }

    private val userDataChangeReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent?) {
            //what we want to happen when the broadcast is sent
            if(App.sharedPreferences.isLoggedIn){
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))//set background color through a function in userDataService
                loginBtnNavHeader.text = "Logout"

                MessageService.getChannel(){ complete->
                    if (complete){
                        if (MessageService.channels.count()>0){
                            selectedChannel = MessageService.channels[0]
                            channelAdapter.notifyDataSetChanged()//tell adapter data changed to reload the data
                        }

                    }else{
                        //insert error messages here
                    }
                }
            }
        }
    }

    fun updateWithChannel(){
        mainChannelName.text = "#${selectedChannel?.name}"
        //download messages from channel
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
    fun loginBtnNavClicked(view: View){
        if(App.sharedPreferences.isLoggedIn){
            UserDataService.logout()
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnNavHeader.text = "Login"
        }else{
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

    }

    private val onNewChannel = Emitter.Listener { args ->
        //println(args[0] as String)
        runOnUiThread{//base on the database
            val channelName = args[0] as String
            val channelDes = args[1] as String
            val channelId = args[2] as String

            val newChannel  = Channel(channelName, channelDes, channelId)
            MessageService.channels.add(newChannel)
           channelAdapter.notifyDataSetChanged()
        }
    }

    fun addChannelClicked(view: View){
        if (App.sharedPreferences.isLoggedIn){
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)
            builder
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    //perform some logic when clicked
                    val nameField = dialogView.findViewById<EditText>(R.id.addChannelNameTxt)
                    val desField = dialogView.findViewById<EditText>(R.id.addChannelDesTxt)
                    val channelNmae = nameField.text.toString()
                    val channelDes = desField.text.toString()
                    //create channel with the channel name and description
                    socket.emit("newChannel",channelNmae, channelDes)
                }
                .setNegativeButton("Cancel"){   dialogInterface, i ->
                    //cancel and close the dialog
                }
                .show()
        }
    }
    fun sendMessageBtnClicked(view: View){
        hideKeyboard()
    }
    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }
}

