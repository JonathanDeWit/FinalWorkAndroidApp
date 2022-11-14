package be.ehb.finalworkjonathandewit.Models

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ApiUserRequest {
    companion object{
        val apiUrl = "https://finalworkapi.azurewebsites.net"
        private val LOGIN_REQUEST_TAG = "LoginRequest"
        private val GET_USER_DATA_REQUEST_TAG = "GetUserDataRequest"

        suspend fun login(loginUser: LoginUser, queue: RequestQueue, errorRequest:RequestError) = suspendCoroutine<String> { jwtToken ->

            val requestUrl = "$apiUrl/api/user/login"

            val loginJson = Gson().toJson(loginUser)
            Log.e(LOGIN_REQUEST_TAG, loginJson)

            val loginRequest = JsonObjectRequest(
                Request.Method.POST, requestUrl, JSONObject(loginJson),
                { response ->
                    Log.e(LOGIN_REQUEST_TAG, response.toString())
                    val token = Gson().fromJson(response.toString(), JwtToken::class.java)
                    jwtToken.resume(token.jwtToken)
                }, { error ->
                    Log.e(LOGIN_REQUEST_TAG, error.message.toString())
                    if(error.networkResponse != null){
                        errorRequest.errorCode = error.networkResponse.statusCode
                        //jwtToken.resume(error.networkResponse.statusCode.toString())
                        jwtToken.resume("")
                    }else{
                        errorRequest.errorCode = 1
                        jwtToken.resume("")
                    }
                })
            loginRequest.tag = LOGIN_REQUEST_TAG
            queue.add(loginRequest)
        }

        suspend fun getUserInformation(apiKey:String, queue: RequestQueue, errorRequest:RequestError) = suspendCoroutine<User> { apiUser ->
            val requestUrl = "$apiUrl/api/user/getInfo"

            val getUserDataRequest = object:JsonObjectRequest(
                Request.Method.GET, requestUrl, null,
                { response ->
                    Log.e(GET_USER_DATA_REQUEST_TAG, response.toString())
                    val user = Gson().fromJson(response.toString(), User::class.java)
                    user.apiKey = apiKey
                    apiUser.resume(user)
                }, { error ->
                    Log.e(GET_USER_DATA_REQUEST_TAG, error.message.toString())
                    Log.e(GET_USER_DATA_REQUEST_TAG, apiKey)
                    if(error.networkResponse != null){
                        errorRequest.errorCode = error.networkResponse.statusCode
                        apiUser.resume(User())
                    }else{
                        errorRequest.errorCode = 1
                        apiUser.resume(User())
                    }
                })
            {
                //Adding Jwt token to header
                //Inspired by Angel Tellez 2019 stackoverflow post (https://stackoverflow.com/questions/51819176/how-to-add-a-custom-header-in-a-volley-request-with-kotlin)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $apiKey"
                    return headers
                }
            }
            Log.e("request", getUserDataRequest.headers.toString())


            getUserDataRequest.tag = LOGIN_REQUEST_TAG
            queue.add(getUserDataRequest)


            suspend fun registUser(queue: RequestQueue, errorRequest:RequestError) = suspendCoroutine<User> { apiUser ->

            }

            suspend fun deleteUser(queue: RequestQueue, errorRequest:RequestError){

            }
        }
    }
}