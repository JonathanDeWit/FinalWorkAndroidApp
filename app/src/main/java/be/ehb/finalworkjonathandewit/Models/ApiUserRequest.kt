package be.ehb.finalworkjonathandewit.Models

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ApiUserRequest {
    companion object{
        val apiUrl = "https://finalworkapi.azurewebsites.net"
        private val LOGIN_REQUEST_TAG = "LoginRequest"
        private val GET_USER_DATA_REQUEST_TAG = "GetUserDataRequest"
        private val DELETE_USER_TAG = "DeleteUserRequest"
        private val REGIST_USER_TAG = "RegistUserRequest"

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
                }
            )

            loginRequest.tag = LOGIN_REQUEST_TAG
            queue.add(loginRequest)
        }

        suspend fun getUserInformation(apiKey:String, apiKeyDate:Date, queue: RequestQueue, errorRequest:RequestError) = suspendCoroutine<User> { apiUser ->
            if (User.isTokenStilValide(apiKeyDate)){
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

                getUserDataRequest.tag = LOGIN_REQUEST_TAG
                queue.add(getUserDataRequest)
            }else{
                //ask new token
            }
        }
        suspend fun registUser(queue: RequestQueue, errorRequest:RequestError, user: RegistUser) = suspendCoroutine<Boolean> { createStatus ->
            val requestUrl = "$apiUrl/api/user/regist"

            val loginJson = Gson().toJson(user)
            Log.e(LOGIN_REQUEST_TAG, loginJson)

            val loginRequest = JsonObjectRequest(
                Request.Method.POST, requestUrl, JSONObject(loginJson),
                { response ->
                    Log.e(LOGIN_REQUEST_TAG, response.toString())
                    createStatus.resume(true)
                }, { error ->
                    Log.e(LOGIN_REQUEST_TAG, error.message.toString())
                    if(error.networkResponse != null){
                        errorRequest.errorCode = error.networkResponse.statusCode
                        createStatus.resume(false)
                    }else{
                        errorRequest.errorCode = 1
                        createStatus.resume(false)
                    }
                }
            )

            loginRequest.tag = LOGIN_REQUEST_TAG
            queue.add(loginRequest)
        }

        suspend fun deleteUser(queue: RequestQueue, errorRequest:RequestError, loginUser: LoginUser, apiKey: String, apiKeyDate:Date)= suspendCoroutine<Boolean>{ deleteStatus ->
            if (User.isTokenStilValide(apiKeyDate)){
                val requestUrl = "$apiUrl/api/user/delete"

                val loginJson = Gson().toJson(loginUser)
                val jUser = JSONObject()
                Log.e("Update", loginJson)

                val userDeleteRequest = object:JsonObjectRequest(
                    Request.Method.POST, requestUrl, JSONObject(loginJson),
                    { response ->
                        Log.e(DELETE_USER_TAG, "Succeed:".plus(response.toString()))
                        deleteStatus.resume(true)
                    }, { error ->
                        Log.e(DELETE_USER_TAG, error.toString())
                        if(error.networkResponse != null){
                            errorRequest.errorCode = error.networkResponse.statusCode
                            Log.e(DELETE_USER_TAG, errorRequest.errorCode.toString())
                            deleteStatus.resume(false)
                        }else{
                            errorRequest.errorCode = 1
                            deleteStatus.resume(false)
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

                userDeleteRequest.tag = LOGIN_REQUEST_TAG
                queue.add(userDeleteRequest)
            }
            else{
                //ask new token
            }
        }

    }
}