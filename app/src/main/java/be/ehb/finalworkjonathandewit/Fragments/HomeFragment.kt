package be.ehb.finalworkjonathandewit.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import be.ehb.finalworkjonathandewit.Activitys.MainActivity
import be.ehb.finalworkjonathandewit.Models.ApiUserRequest
import be.ehb.finalworkjonathandewit.Models.RequestError
import be.ehb.finalworkjonathandewit.Models.SysStatus
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(R.layout.fragment_home) {


    private val applicationViewModels: ApplicationViewModels by activityViewModels {
        UserViewModelFactory((activity?.application as SecurityApplication).repository, requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var errorTextView = view.findViewById<TextView>(R.id.homeErrorTextView)
        var liveVideoButton = view.findViewById<Button>(R.id.liveVideoButton)
        var statusButton = view.findViewById<Button>(R.id.OnOffButton)

        var sysStatus = SysStatus()
        lifecycleScope.launch() {
            errorTextView.text=""
            val error = RequestError()
            var user = applicationViewModels.dbUser

            if (user.apiKey.isNotBlank()){
                withContext(Dispatchers.IO) {
                    sysStatus = ApiUserRequest.getSysStatus(user.apiKey, user.apiKeyDate, applicationViewModels.queue, error)
                }
            }
            if (error.errorCode<=0){
                updateButtonStatus(statusButton, sysStatus.SysState)
            }
            else{
                errorTextView.text= getString(R.string.unable_to_get_system_state)
            }
        }




        statusButton.setOnClickListener {
            statusButton.isEnabled = false

            sysStatus.SysState =! sysStatus.SysState
            updateButtonStatus(statusButton, sysStatus.SysState)

            var liveVideoButton = view.findViewById<Button>(R.id.liveVideoButton)
            var statusButton = view.findViewById<Button>(R.id.OnOffButton)

            lifecycleScope.launch() {
                val error = RequestError()
                var user = applicationViewModels.dbUser

                if (user.apiKey.isNotBlank()){
                    withContext(Dispatchers.IO) {
                        ApiUserRequest.updateSystemStatus(user.apiKey, user.apiKeyDate, applicationViewModels.queue,
                            error, sysStatus.SysState)
                    }
                }
                if (error.errorCode<=0){
                    updateButtonStatus(statusButton, sysStatus.SysState)
                }
                else{
                    sysStatus.SysState =! sysStatus.SysState
                    errorTextView.text= getString(R.string.unable_to_change_system_state)
                }

                statusButton.isEnabled = true
            }
        }


        liveVideoButton.setOnClickListener {

            (activity as MainActivity?)?.disableNavBar()

            val action = HomeFragmentDirections.actionHomeFragmentToLiveVideoFragment2()
            findNavController().navigate(action)
        }
    }


    fun updateButtonStatus(button: Button, status: Boolean){
        if (status){
            button.text = getString(R.string.turn_off_the_system)
        }
        else{
            button.text = getString(R.string.activate_the_system)
        }
    }
}