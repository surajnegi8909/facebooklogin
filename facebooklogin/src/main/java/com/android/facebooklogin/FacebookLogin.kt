package com.android.facebooklogin

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistryOwner
import com.android.facebooklogin.util.FacebookLoginListener
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class FacebookLogin() {
    companion object {
        private var facebookLoginListener: FacebookLoginListener? = null
        private var mCallbackManager: CallbackManager? = null
        lateinit var activityResultOwner : ActivityResultRegistryOwner

        fun doFacebookLogin(context: Context?, activityResultRegistryOwner: ActivityResultRegistryOwner,facebookListener:FacebookLoginListener) {
            activityResultOwner = activityResultRegistryOwner
            facebookLoginListener = facebookListener
            printHashKey(context)
            mCallbackManager = create()
            LoginManager.getInstance()
                .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        facebookLoginListener?.onFbLoginSuccess()
                        getUserProfile()
                    }

                    override fun onCancel() {
                        LoginManager.getInstance().logOut()
                        facebookLoginListener?.onFbLoginCancel()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show()
                        facebookLoginListener?.onFbLoginError()
                    }
                })
        }

        fun performLogin(mContext: Context?) {
            if (isNetworkAvailable(mContext)) {
                LoginManager.getInstance().logOut()
                mCallbackManager?.let {
                    LoginManager.getInstance().logInWithReadPermissions(activityResultOwner,
                        it,
                        listOf("public_profile", "email"))
                }
            } else {
                Toast.makeText(mContext,
                    "Please check your internet connection",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        fun getUserProfile() {
            val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
            ) { userData: JSONObject?, response: GraphResponse? ->
                if (response?.error != null) {
                } else {
                    facebookLoginListener?.onGetProfileSuccess(userData)
                }
            }
            val parameters = Bundle()
            parameters.putString("fields","id,first_name,last_name,email,gender,picture.height(800).width(800)")
            request.parameters = parameters
            request.executeAsync()
        }
        fun facebookSignOut(){
            LoginManager.getInstance().logOut()
        }

        private fun printHashKey(mContext: Context?) {
            try {
                val packageName = mContext?.applicationContext?.packageName
                val info = packageName?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        mContext?.packageManager?.getPackageInfo(
                            it,
                            PackageManager.GET_SIGNATURES
                        )
                    } else {
                        mContext?.packageManager?.getPackageInfo(
                            it,
                            PackageManager.GET_SIGNATURES
                        )
                    }
                }
                if (info != null) {
                    for (signature in info.signatures) {
                        val md = MessageDigest.getInstance("SHA")
                        md.update(signature.toByteArray())
                        Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                    }
                }
            } catch (e: NameNotFoundException) {
                Log.e("FacebookLogin", "Package Name not found")
            } catch (e: NoSuchAlgorithmException) {
                Log.e("FacebookLogin", "No such Algorithm")
            }
        }

        private fun isNetworkAvailable(context: Context?): Boolean {
            return try {
                val connectivityManager =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                activeNetworkInfo != null && activeNetworkInfo.isConnected
            } catch (n: NullPointerException) {
                n.printStackTrace()
                false
            }
        }

    }
}

