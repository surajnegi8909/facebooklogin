package com.android.facebookloginlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.facebooklogin.FacebookLogin
import com.android.facebooklogin.util.FacebookLoginListener
import com.bumptech.glide.Glide
import org.json.JSONObject
import kotlin.math.sign

class MainActivity : AppCompatActivity() ,FacebookLoginListener{

    lateinit var loginButton:Button
    lateinit var signOutButton:Button
    lateinit var textView: TextView
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FacebookLogin.doFacebookLogin(this,this,this)

        loginButton= findViewById(R.id.btnFbLogin)
        signOutButton= findViewById(R.id.button2)
        textView= findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        signOutButton.visibility= View.GONE
        imageView.visibility= View.GONE

        loginButton.setOnClickListener {
            FacebookLogin.performLogin()
        }

        signOutButton.setOnClickListener {
            FacebookLogin.facebookSignOut()
            signOutButton.visibility= View.GONE
            imageView.visibility= View.GONE
            loginButton.visibility= View.VISIBLE

            textView.text= ""
        }
    }

    override fun onFbLoginSuccess() {
        loginButton.visibility= View.GONE
    }

    override fun onFbLoginCancel() {
        Toast.makeText(this,"cancelled by the user.",Toast.LENGTH_SHORT).show()
    }

    override fun onFbLoginError() {
        System.err.println("error >>>>>>> ")
    }

    override fun onGetProfileSuccess(userData: JSONObject?) {

        System.err.println("user data id ${userData?.getString("id")}")
        System.err.println("user data firstname ${userData?.getString("first_name")}")
        System.err.println("user data lastname  ${userData?.getString("last_name")}")
        val username = "${userData?.getString("first_name")} ${userData?.getString("last_name")}"
        if (userData?.has("email") == true) {
            System.err.println("user data email ${userData.getString("email")}")
        }
        if (userData?.has("gender") == true) {
            System.err.println("user data gender ${userData.getString("gender")}")
        }
        val url= userData?.getJSONObject("picture")
        val url1= url?.getJSONObject("data")
        val url2 = url1?.getString("url")
        textView.text= username
        signOutButton.visibility= View.VISIBLE
        imageView.visibility= View.VISIBLE
        Glide.with(this).load(url2).into(imageView)
        System.err.println("user data  $userData")
    }
}