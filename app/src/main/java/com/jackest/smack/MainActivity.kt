package com.jackest.smack

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

//    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle  = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show() //similar to toast, pop up message
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
    fun loginBtnNavClicked(view: View){
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }
    fun addChannelClicked(view: View){

    }
    fun sendMessageBtnClicked(view: View){

    }
}

