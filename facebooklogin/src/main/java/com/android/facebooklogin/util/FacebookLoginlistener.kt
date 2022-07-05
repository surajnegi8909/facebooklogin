package com.android.facebooklogin.util

import org.json.JSONObject

interface FacebookLoginListener {
    fun onFbLoginSuccess()
    fun onFbLoginCancel()
    fun onFbLoginError()
    fun onGetProfileSuccess(userData: JSONObject?)
}