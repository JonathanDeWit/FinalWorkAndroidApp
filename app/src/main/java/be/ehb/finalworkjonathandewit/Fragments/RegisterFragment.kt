package be.ehb.finalworkjonathandewit.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import be.ehb.finalworkjonathandewit.Models.*
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val applicationViewModels: ApplicationViewModels by activityViewModels {
        UserViewModelFactory((activity?.application as SecurityApplication).repository, requireActivity())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val signUpButton = view.findViewById<Button>(R.id.signUpButton)
        val firstNameEditText = view.findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = view.findViewById<EditText>(R.id.lastNameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.confirmPasswordEditText)
        val errorTextView = view.findViewById<TextView>(R.id.registerErrorTextView)

        signUpButton.setOnClickListener {
            errorTextView.text=""

            val newUser = RegistUser(
                emailEditText.text.toString(),
                firstNameEditText.text.toString(),
                lastNameEditText.text.toString(),
                passwordEditText.text.toString()
            )

            if (newUser.isNotEmpty()){
                if (newUser.Password == confirmPasswordEditText.text.toString()){

                    lifecycleScope.launch {
                        var error = RequestError()

                        withContext(Dispatchers.IO) {
                            ApiUserRequest.registUser(applicationViewModels.queue, error, newUser)
                        }
                        withContext(Dispatchers.Main) {
                            if (error.errorCode<=0){
                                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(newUser.Email, newUser.Password)
                                findNavController().navigate(action)
                            }
                            else{
                                errorTextView.text = getString(R.string.loginError)
                            }
                        }
                    }
                }else{
                    errorTextView.text = getString(R.string.same_password)
                }
            }
            else{
                errorTextView.text = getString(R.string.no_empty_fields)
            }
        }
    }
}