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
import be.ehb.finalworkjonathandewit.Models.LoginUser
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.LoginViewModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val applicationViewModels: ApplicationViewModels by activityViewModels()
        val loginViewModel: LoginViewModels by activityViewModels()




        var loginButton = view.findViewById<Button>(R.id.loginButton)
        var errorTextView = view.findViewById<TextView>(R.id.errorTextView)

        var userNameInput = view.findViewById<EditText>(R.id.editTextTextUserName)
        var passwordInput = view.findViewById<EditText>(R.id.editTextTextPassword)


        loginButton.setOnClickListener {
            loginButton.isEnabled = false
            errorTextView.text=""
            lifecycleScope.launch {
                var jwtToken = ""
                //var loginUser = LoginUser("jonathan.de.wit@gmail.be", "Jonathan!014741212")
                var loginUser = LoginUser(userNameInput.text.toString(), passwordInput.text.toString())
                withContext(Dispatchers.IO) {
                    jwtToken = loginViewModel.login(loginUser, applicationViewModels.getQueue(activity))
                }
                withContext(Dispatchers.Main) {
                    if (jwtToken.length>3){
                        Log.e("API_Request_Login", "After request")
                        endLogin()
                    }
                    else{
                        if (jwtToken.equals("400")){
                            errorTextView.text = getString(R.string.userAndPasswordNotMatch)
                        }else if(jwtToken.equals("408")){
                            errorTextView.text = getString(R.string.manyWrongAttemps)
                        }else{
                            errorTextView.text = getString(R.string.loginError)
                        }
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
}