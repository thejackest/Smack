package com.jackest.smack.Utilities

const val SOCKET_URL = ""
const val BASE_URL = "" //URL in HEROKU
//or if its for the local host

const val URL_REGISTER = "${BASE_URL}account/register"

const val URL_LOGIN = "${BASE_URL}account/login"

const val URL_CREATE_USER = "${BASE_URL}account/add"

const val URL_GET_USER = "${BASE_URL}user/byEmail"

const val URL_GET_CHANNELS = "${BASE_URL}channel"
//Broadcast constants
const val BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"