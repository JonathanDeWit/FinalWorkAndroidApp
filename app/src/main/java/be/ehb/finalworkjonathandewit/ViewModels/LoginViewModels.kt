package be.ehb.finalworkjonathandewit.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import be.ehb.finalworkjonathandewit.Activitys.MainActivity
import be.ehb.finalworkjonathandewit.Fragments.LoginFragmentDirections
import be.ehb.finalworkjonathandewit.Model.JwtToken
import be.ehb.finalworkjonathandewit.Model.LoginUser
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.JsonObject
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
                Log.e("API_Request_Login", error.localizedMessage)
                jwtToken.resume("")
            })

        loginRequest.tag = LOGIN_REQUEST_TAG
        queue.add(loginRequest)

    }
}