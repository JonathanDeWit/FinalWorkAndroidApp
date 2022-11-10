package be.ehb.finalworkjonathandewit.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import be.ehb.finalworkjonathandewit.Activitys.MainActivity
import be.ehb.finalworkjonathandewit.R
import androidx.navigation.fragment.findNavController
import be.ehb.finalworkjonathandewit.Model.LoginUser
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.LoginViewModels
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applicationViewModels: ApplicationViewModels by activityViewModels()
        val loginViewModel: LoginViewModels by activityViewModels()




        var button = view.findViewById<Button>(R.id.loginButton)


        button.setOnClickListener {
            button.isEnabled = false

            lifecycleScope.launch {
                var jwtToken = ""
                withContext(Dispatchers.IO) {
                    jwtToken = loginViewModel.login(LoginUser("jonathan.de.wit@gmail.com", "Jonathan!014741212"), applicationViewModels.getQueue(activity))
                }
                withContext(Dispatchers.Main) {
                    if (jwtToken.isNotEmpty()){
                        Log.e("API_Request_Login", "After request")
                        endLogin()
                    }
                    else{
                        button.isEnabled = true
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