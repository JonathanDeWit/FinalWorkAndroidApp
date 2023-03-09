package be.ehb.finalworkjonathandewit.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import be.ehb.finalworkjonathandewit.Models.ApiUserRequest
import be.ehb.finalworkjonathandewit.Models.DeviceType
import be.ehb.finalworkjonathandewit.Models.LoginUser
import be.ehb.finalworkjonathandewit.Models.RequestError
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddDeviceFragment: Fragment(R.layout.fragment_add_device) {


    private val applicationViewModels: ApplicationViewModels by activityViewModels {
        UserViewModelFactory((activity?.application as SecurityApplication).repository, requireActivity())
    }


    private lateinit var errorAddDeviceTextView: TextView
    private lateinit var addDeviceButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        errorAddDeviceTextView = view.findViewById<TextView>(R.id.errorAddDeviceTextView)
        var deviceUserNameEditText = view.findViewById<EditText>(R.id.deviceUserNameEditText)
        var devicePasswordEditText = view.findViewById<EditText>(R.id.devicePasswordEditText)
        addDeviceButton = view.findViewById<Button>(R.id.addDeviceButton)


        addDeviceButton.setOnClickListener {
            var userName = deviceUserNameEditText.text.toString()
            var password = devicePasswordEditText.text.toString()

            if (userName.isNotBlank() && password.isNotBlank()){
                updateName(userName, password)
            }
            else{
                errorAddDeviceTextView.text = getString(R.string.no_empty_fields)
            }

        }
    }

    fun updateName(userName: String, password: String){
        addDeviceButton.isEnabled = false
        errorAddDeviceTextView.text = ""

        lifecycleScope.launch() {
            val error = RequestError()
            var user = applicationViewModels.dbUser

            if (user.apiKey.isNotBlank()){
                withContext(Dispatchers.IO) {
                    ApiUserRequest.addDevice(LoginUser(userName, password), user.apiKey,
                        user.apiKeyDate, applicationViewModels.queue, error)
                }
            }
            if (error.errorCode<=0){
                // sucseed
                errorAddDeviceTextView.text = getString(R.string.login)
                context?.let {
                    errorAddDeviceTextView.setTextColor(ContextCompat.getColor(it, R.color.button_on))
                }


            }
            else{
                errorAddDeviceTextView.text = getString(R.string.adding_device_failed);
                context?.let {
                    errorAddDeviceTextView.setTextColor(ContextCompat.getColor(it, R.color.button_off))
                }
            }

            errorAddDeviceTextView.isEnabled = true
        }
    }
}