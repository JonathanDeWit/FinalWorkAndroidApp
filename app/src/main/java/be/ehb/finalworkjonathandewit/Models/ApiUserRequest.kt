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

        suspend fun login(loginUser: LoginUser, queue: RequestQueue, errorRequest:RequestError) = suspendCoroutine<String> { jwtToken ->

            val requestUrl = "$apiUrl/api/user/login"

            val loginJson = Gson().toJson(loginUser)
            Log.e("Test",loginJson.toString().toByteArray().size.toString())
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


            Log.e("header", loginRequest.headers.toString())

            loginRequest.tag = LOGIN_REQUEST_TAG
            queue.add(loginRequest)
        }

        suspend fun getUserInformation(apiKey:String, apiKeyDate:Date, queue: RequestQueue, errorRequest:RequestError) = suspendCoroutine<User> { apiUser ->
            if (isTokenStilValide(apiKeyDate)){
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

//                if(getUserDataRequest.headers is HashMap<String, String>){
//                    getUserDataRequest.headers["Authorization"]="Bearer $apiKey"
//                }

                Log.e("request", getUserDataRequest.headers.toString())


                getUserDataRequest.tag = LOGIN_REQUEST_TAG
                queue.add(getUserDataRequest)
            }
        }
        suspend fun registUser(queue: RequestQueue, errorRequest:RequestError, user: registUser) = suspendCoroutine<User> { apiUser ->

        }

        suspend fun deleteUser(queue: RequestQueue, errorRequest:RequestError, loginUser: LoginUser, apiKey: String, apiKeyDate:Date)= suspendCoroutine<Boolean>{ deleteStatus ->
            loginUser.password = "Jonathan014741"
            if (isTokenStilValide(apiKeyDate)){
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
        }


//        suspend fun deleteA(queue: RequestQueue, errorRequest:RequestError)= suspendCoroutine<LoginUser>{ deleteStatus ->
//            val requestUrl = "$apiUrl/api/user/deletea"
//
//            //val loginJson = Gson().toJson(loginUser)
//            Log.e("DeleteA", "Start")
//
//            val userDeleteRequest = object:JsonObjectRequest(
//                Request.Method.DELETE, requestUrl, null,
//                { response ->
//                    Log.e("DeleteA", response.toString())
//                    val user = Gson().fromJson(response.toString(), LoginUser::class.java)
//                    deleteStatus.resume(user)
//                }, { error ->
//                    Log.e(DELETE_USER_TAG, error.message.toString())
//                    if(error.networkResponse != null){
//                        errorRequest.errorCode = error.networkResponse.statusCode
//                        deleteStatus.resume(LoginUser("", ""))
//                    }else{
//                        errorRequest.errorCode = 1
//                        deleteStatus.resume(LoginUser("", ""))
//                    }
//                })
//            {
//            }
//
//            userDeleteRequest.tag = LOGIN_REQUEST_TAG
//            queue.add(userDeleteRequest)
//        }
//
//        suspend fun deleteB(queue: RequestQueue, errorRequest:RequestError, loginUser: LoginUser)= suspendCoroutine<LoginUser>{ deleteStatus ->
//            val requestUrl = "$apiUrl/api/user/postb"
//
//            val loginJson = Gson().toJson(loginUser)
//            Log.e("DeleteB", "Start")
//
//
//            var test = "{\"Email\":\"jonathan.de.wit@gmail.be\",\"Password\":\"Jonathan014741\"}"
//            Log.e("DeleteB", JSONObject(test).toString())
//
//            val userDeleteRequest = JsonObjectRequest(
//                Request.Method.POST, requestUrl, JSONObject(test),
//                { response ->
//                    Log.e("DeleteB", response.toString())
//                    val user = Gson().fromJson(response.toString(), LoginUser::class.java)
//                    deleteStatus.resume(user)
//                }, { error ->
//                    Log.e(DELETE_USER_TAG, error.message.toString())
//                    if(error.networkResponse != null){
//                        errorRequest.errorCode = error.networkResponse.statusCode
//                        deleteStatus.resume(LoginUser("", ""))
//                    }else{
//                        errorRequest.errorCode = 1
//                        deleteStatus.resume(LoginUser("", ""))
//                    }
//                })
//
//            userDeleteRequest.tag = LOGIN_REQUEST_TAG
//            queue.add(userDeleteRequest)
//        }





        fun isTokenStilValide(apiKeyDate:Date):Boolean{

            val apikeyEndDate = Date(apiKeyDate.time+(14*60*1000))
            val now = Date()

            //inspired by https://www.techiedelight.com/compare-two-dates-in-kotlin/
            val cmp = apikeyEndDate.compareTo(now)
            when{
                cmp > 0 -> {
                    return true
                }
                cmp < 0 -> {
                    return true
                }
                else ->{
                    return false
                }
            }

        }
    }
}