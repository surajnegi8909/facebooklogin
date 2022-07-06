# Facebook login library

Features:
Facebook Login and Get User Details

Now open your project in which you want to use this library.

Add it in your root build.gradle at the end of repositories:

```allprojects {
repositories {
...
maven { url 'https://jitpack.io' }
}
}```

```dependencies {
implementation 'com.github.surajnegi8909:facebooklogin:0.0.1'
}```

>After Successfully importing facebooklogin module, Now let's implement it.

>Create app in https://developers.facebook.com and add product Facebook Login (setup app).
>Goto  Settings -> Basic and copy App ID and App Secret.

>Goto res -> values folder and open string.xml file.
>Paste these lines and replace APP_ID and APP_SECRET.
```<string name="facebook_app_id">APP_ID</string>
   <string name="facebook_client_token">APP_SECRET</string>```
>Close string.xml file.

>Now open AndroidManifest.xml file 
>Paste these permission outside the application tag.

```<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   <uses-permission android:name="android.permission.INTERNET"/>```

>Paste these lines outside the activity tag.
```<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
   <meta-data android:name="com.facebook.sdk.ClientToken" android:value="@string/facebook_client_token"/>```
>Close AndroidManifest.xml file.

>Open the Activity in which you want to implement Facebook Login.

>implement FacebookLoginListener interface and import all function.

>Inside onCreate function.
```FacebookLogin.doFacebookLogin(this,this,this)```
>Respond to a login result, we need to register a callback.
//here we are initializing, adding LoginManager callback and setting up FacebookLoginListener.

>Add performLogin function when we click on login with facebook button 
```loginWithFacebook.setOnClickListener{
FacebookLogin.performLogin(this)
}```

>You will get user data inside onGetProfileSuccess(userData: JSONObject?). You can parse it as you like.
>To see user data you can print userData.





