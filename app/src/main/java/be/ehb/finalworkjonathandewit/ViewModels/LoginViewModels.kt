package be.ehb.finalworkjonathandewit.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import be.ehb.finalworkjonathandewit.Models.JwtToken
import be.ehb.finalworkjonathandewit.Models.LoginUser
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume

class LoginViewModels : ViewModel()  {
    val apiUrl = "https://finalworkapi.azurewebsites.net"
    private val LOGIN_REQUEST_TAG = "LoginRequest"

    suspend fun login(loginUser: LoginUser, queue: RequestQueue)= suspendCoroutine<String> { jwtToken ->
        //var listener = Response.Listener<JSONObject>.onResponse(response -> { })
        val requestUrl = "$apiUrl/api/user/login"

        val loginJson = Gson().toJson(loginUser)
        Log.e("API_Request_Login", loginJson)

        val loginRequest = JsonObjectRequest(
            Request.Method.POST, requestUrl, JSONObject(loginJson),
            { response ->
                Log.e("API_Request_Login", response.toString())
                val token = Gson().fromJson(response.toString(), JwtToken::class.java)
                jwtToken.resume(token.jwtToken)
            }, { error ->
                Log.e("API_Request_Login", error.message.toString())
                if(error.networkResponse != null){
                    if (error.networkResponse.statusCode == 408){
                        Log.e("API_Request_Login", "408")
                        jwtToken.resume("408")
                    }
                    else if (error.networkResponse.statusCode == 400){
                        Log.e("API_Request_Login", "400")
                        jwtToken.resume("400")
                    }
                    else{
                        Log.e("API_Request_Login", "000")
                        jwtToken.resume("000")
                    }
                }else{
                    jwtToken.resume("000")
                }
            })

        loginRequest.tag = LOGIN_REQUEST_TAG
        queue.add(loginRequest)

    }
}