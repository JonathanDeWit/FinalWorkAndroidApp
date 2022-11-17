package be.ehb.finalworkjonathandewit.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import be.ehb.finalworkjonathandewit.Activitys.MainActivity
import be.ehb.finalworkjonathandewit.R
import androidx.navigation.fragment.findNavController
import be.ehb.finalworkjonathandewit.Models.ApiUserRequest
import be.ehb.finalworkjonathandewit.Models.LoginUser
import be.ehb.finalworkjonathandewit.Models.RequestError
import be.ehb.finalworkjonathandewit.Models.User
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.LoginViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginFragment : Fragment(R.layout.fragment_login) {


    private val applicationViewModels: ApplicationViewModels by activityViewModels {
        UserViewModelFactory((activity?.application as SecurityApplication).repository, requireActivity())
    }
    private val loginViewModel: LoginViewModels by activityViewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        var loginButton = view.findViewById<Button>(R.id.loginButton)
        var errorTextView = view.findViewById<TextView>(R.id.errorTextView)

        var userNameInput = view.findViewById<EditText>(R.id.editTextTextUserName)
        var passwordInput = view.findViewById<EditText>(R.id.enterPasswordEditText)




        loginButton.setOnClickListener {
            loginButton.isEnabled = false
            errorTextView.text=""
            lifecycleScope.launch {
                var error = RequestError()
                var jwtToken = ""
                var user:User = User()
                var loginUser = LoginUser("jonathan.de.wit@gmail.be", "Jonathan014741")
                //var loginUser = LoginUser(userNameInput.text.toString(), passwordInput.text.toString())

                withContext(Dispatchers.IO) {
                    jwtToken = ApiUserRequest.login(loginUser, applicationViewModels.queue, error)
                }
                if (error.errorCode<=0){
                    withContext(Dispatchers.IO) {
                        user = ApiUserRequest.getUserInformation(jwtToken, user.apiKeyDate, applicationViewModels.queue, error)
                    }
                }
                if (error.errorCode<=0 && applicationViewModels.dbUsers < 1){
                    withContext(Dispatchers.IO) {
                        applicationViewModels.insert(user)
                    }
                }
                else{
                    withContext(Dispatchers.IO) {
                        applicationViewModels.update(user)
                    }
                }
                withContext(Dispatchers.Main) {
                    if (error.errorCode<=0){

                        endLogin()
                    }
                    else{
                        showError(error, errorTextView)
                        userNameInput.text.clear()
                        passwordInput.text.clear()
                        loginButton.isEnabled = true
                    }
                }
            }
        }
    }

    private fun endLogin(){
        (activity as MainActivity?)?.enableNavBar()

        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun showError(error:RequestError, errorTextView:TextView){
        if (error.errorCode == 400){
            errorTextView.text = getString(R.string.userAndPasswordNotMatch)
        }else if(error.errorCode == 408){
            errorTextView.text = getString(R.string.manyWrongAttemps)
        }else if(error.errorCode == 401){
            errorTextView.text = getString(R.string.UnauthorizedError)
        }else{
            errorTextView.text = getString(R.string.loginError)
        }
    }






}