package be.ehb.finalworkjonathandewit.Models

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
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
        private val SysStatus_TAG = "SysStatusRequest"
        private val UpdateSysStatus_TAG = "UpdateSysStatusRequest"

        suspend fun login(loginUser: LoginUser, queue: RequestQueue, errorRequest:RequestError) = suspendCoroutine<String> { jwtToken ->

            val requestUrl = "$apiUrl/api/user/login"

            val loginJson = Gson().toJson(loginUser)
            Log.i(LOGIN_REQUEST_TAG, loginJson)

            val loginRequest = JsonObjectRequest(
                Request.Method.POST, requestUrl, JSONObject(loginJson),
                { response ->
                    Log.i(LOGIN_REQUEST_TAG, response.toString())
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
                        Log.i(GET_USER_DATA_REQUEST_TAG, response.toString())
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

        suspend fun updateSystemStatus(apiKey:String, apiKeyDate:Date, queue: RequestQueue, errorRequest:RequestError, newStatus: Boolean) = suspendCoroutine<Boolean> { apiUser ->
            if (User.isTokenStilValide(apiKeyDate)){
                val requestUrl = "$apiUrl/api/user/update/SystemState?newStatus=${newStatus.toString()}"
                Log.i(UpdateSysStatus_TAG, requestUrl)
                val updateSysStatusRequest = object : StringRequest(
                    Request.Method.POST, requestUrl,
                    Response.Listener<String> { response ->
                        Log.i(UpdateSysStatus_TAG, response.toString())
                        apiUser.resume(true)
                    },
                    Response.ErrorListener { error ->
                        Log.e(UpdateSysStatus_TAG, error.message.toString())
                        if(error.networkResponse != null){
                            errorRequest.errorCode = error.networkResponse.statusCode
                            apiUser.resume(false)
                        }else{
                            errorRequest.errorCode = 1
                            apiUser.resume(false)
                        }
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer $apiKey"
                        return headers
                    }
                }
                updateSysStatusRequest.tag = UpdateSysStatus_TAG
                queue.add(updateSysStatusRequest)
            }else{
            }
        }

        suspend fun updateDeviceName(apiKey:String, apiKeyDate:Date, queue: RequestQueue, errorRequest:RequestError, cameraId:Int, name: String, deviceType: DeviceType) = suspendCoroutine<Boolean> { apiUser ->
            if (User.isTokenStilValide(apiKeyDate)){
                val requestUrl = "$apiUrl/api/user/update/${deviceType}/name?deviceId=${cameraId}&name=${name}"
                Log.i(UpdateSysStatus_TAG, requestUrl)
                val updateSysStatusRequest = object : StringRequest(
                    Request.Method.POST, requestUrl,
                    Response.Listener<String> { response ->
                        Log.i(UpdateSysStatus_TAG, response.toString())
                        apiUser.resume(true)
                    },
                    Response.ErrorListener { error ->
                        Log.e(UpdateSysStatus_TAG, error.message.toString())
                        if(error.networkResponse != null){
                            errorRequest.errorCode = error.networkResponse.statusCode
                            apiUser.resume(false)
                        }else{
                            errorRequest.errorCode = 1
                            apiUser.resume(false)
                        }
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer $apiKey"
                        return headers
                    }
                }
                updateSysStatusRequest.tag = UpdateSysStatus_TAG
                queue.add(updateSysStatusRequest)
            }else{
            }
        }

        suspend fun updateDeviceLocation(apiKey:String, apiKeyDate:Date, queue: RequestQueue, errorRequest:RequestError, cameraId:Int, name: String, deviceType: DeviceType) = suspendCoroutine<Boolean> { apiUser ->
            if (User.isTokenStilValide(apiKeyDate)){
                val requestUrl = "$apiUrl/api/user/update/${deviceType}/location?deviceId=${cameraId}&location=${name}"
                Log.i(UpdateSysStatus_TAG, requestUrl)
                val updateSysStatusRequest = object : StringRequest(
                    Request.Method.POST, requestUrl,
                    Response.Listener<String> { response ->
                        Log.i(UpdateSysStatus_TAG, response.toString())
                        apiUser.resume(true)
                    },
                    Response.ErrorListener { error ->
                        Log.e(UpdateSysStatus_TAG, error.message.toString())
                        if(error.networkResponse != null){
                            errorRequest.errorCode = error.networkResponse.statusCode
                            apiUser.resume(false)
                        }else{
                            errorRequest.errorCode = 1
                            apiUser.resume(false)
                        }
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer $apiKey"
                        return headers
                    }
                }
                updateSysStatusRequest.tag = UpdateSysStatus_TAG
                queue.add(updateSysStatusRequest)
            }else{
            }
        }

        suspend fun getSysStatus(apiKey:String, apiKeyDate:Date, queue: RequestQueue, errorRequest:RequestError) = suspendCoroutine<SysStatus> { sysStatus ->
            if (User.isTokenStilValide(apiKeyDate)){
                val requestUrl = "$apiUrl/api/user/getSystemState"

                val getUserDataRequest = object:JsonObjectRequest(
                    Request.Method.GET, requestUrl, null,
                    { response ->
                        Log.i(SysStatus_TAG, response.toString())
                        val status = Gson().fromJson(response.toString(), SysStatus::class.java)
                        Log.i(SysStatus_TAG, status.SysState.toString())


                        sysStatus.resume(status)
                    }, { error ->
                        Log.e(SysStatus_TAG, error.message.toString())
                        if(error.networkResponse != null){
                            errorRequest.errorCode = error.networkResponse.statusCode
                            sysStatus.resume(SysStatus())
                        }else{
                            errorRequest.errorCode = 1
                            sysStatus.resume(SysStatus())
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

                getUserDataRequest.tag = SysStatus_TAG
                queue.add(getUserDataRequest)
            }else{
                //ask new token
            }
        }



        suspend fun registUser(queue: RequestQueue, errorRequest:RequestError, user: RegistUser) = suspendCoroutine<Boolean> { createStatus ->
            val requestUrl = "$apiUrl/api/user/regist"

            val loginJson = Gson().toJson(user)
            Log.e(REGIST_USER_TAG, loginJson)

            val loginRequest = JsonObjectRequest(
                Request.Method.POST, requestUrl, JSONObject(loginJson),
                { response ->
                    Log.e(REGIST_USER_TAG, response.toString())
                    createStatus.resume(true)
                }, { error ->
                    Log.e(REGIST_USER_TAG, error.message.toString())
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
                Log.e("Delete", loginJson)

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