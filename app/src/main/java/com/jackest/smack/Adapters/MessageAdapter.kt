package com.jackest.smack.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jackest.smack.Model.Message
import com.jackest.smack.R
import com.jackest.smack.Services.UserDataService

class MessageAdapter(val context:Context, val messages : ArrayList<Message>):RecyclerView.Adapter<MessageAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindMessage(context,messages[position])
    }
    inner class ViewHolder(itemView:View?) : RecyclerView.ViewHolder(itemView!!){
        val userImage = itemView?.findViewById<ImageView>(R.id.msgUserImage)
        val time = itemView?.findViewById<TextView>(R.id.timeLbl)
        val userName = itemView?.findViewById<TextView>(R.id.msgUserNameLbl)
        val messageBody = itemView?.findViewById<TextView>(R.id.msgBodyLbl)

        fun bindMessage(context: Context, message: Message){
            val resourceId = context.resources.getIdentifier(message.userAvatar,"drawable",context.packageName)
            userImage?.setImageResource(resourceId)
            userImage?.setBackgroundColor(UserDataService.returnAvatarColor(message.avatarColor))
            userName?.text = message.useName
            time?.text = message.timeStamp
            messageBody?.text = message.message
        }
    }
}