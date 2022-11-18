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

    suspend fun getUserInformation(apiKey:String){

    }

}