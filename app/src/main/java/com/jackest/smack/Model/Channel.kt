package com.jackest.smack.Model

class Channel (val name:String, val des:String, val id: String){
    override fun toString(): String {
        return "#$name" //# because its the chat room
    }
}