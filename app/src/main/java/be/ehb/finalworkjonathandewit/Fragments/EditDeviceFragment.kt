package be.ehb.finalworkjonathandewit.Fragments

import android.location.Location
import android.os.Bundle
import android.util.Log
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
import be.ehb.finalworkjonathandewit.Models.RequestError
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

class EditDeviceFragment: Fragment(R.layout.fragment_edit_device) {



    private val applicationViewModels: ApplicationViewModels by activityViewModels {
        UserViewModelFactory((activity?.application as SecurityApplication).repository, requireActivity())
    }

    private lateinit var errorUpdateDeviceTextView: TextView
    private lateinit var editDeviceButton: Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val args = EditDeviceFragmentArgs.fromBundle(requireArguments())

        var deviceId = args.deviceId
        var deviceType = args.deviceType
        var deviceLocation = args.deviceLocation
        var deviceName = args.deviceName


        var deviceNameEditText = view.findViewById<EditText>(R.id.deviceNameEditText)
        var deviceLocationEditText = view.findViewById<EditText>(R.id.deviceLocationEditText)
        editDeviceButton = view.findViewById<Button>(R.id.editDeviceButton)
        errorUpdateDeviceTextView = view.findViewById<TextView>(R.id.errorUpdateDeviceTextView)

        if (deviceName != null) {
            deviceNameEditText.setText(deviceName)
        }

        if (deviceLocationEditText != null) {
            deviceLocationEditText.setText(deviceLocation)
        }


        editDeviceButton.setOnClickListener {
            if (!deviceNameEditText.text.equals(String())){
                if (deviceName != null) {
                    if (!deviceName.equals(deviceNameEditText.text.toString())){
                        updateName(deviceId, deviceNameEditText.text.toString(), deviceType)
                    }
                }
                else{
                    updateName(deviceId, deviceNameEditText.text.toString(), deviceType)
                }
            }


            if (!deviceLocationEditText.text.equals(String())){
                if (deviceLocation != null) {
                    if (!deviceLocation.equals(deviceLocationEditText.text.toString())){
                        updateLocation(deviceId, deviceLocationEditText.text.toString(), deviceType)
                    }
                }
                else{
                    updateLocation(deviceId, deviceLocationEditText.text.toString(), deviceType)
                }
            }
        }
    }


    fun updateName(cameraId:Int, name: String, deviceType: DeviceType){
        editDeviceButton.isEnabled = false

        lifecycleScope.launch() {
            val error = RequestError()
            var user = applicationViewModels.dbUser

            if (user.apiKey.isNotBlank()){
                withContext(Dispatchers.IO) {
                    ApiUserRequest.updateDeviceName(user.apiKey, user.apiKeyDate, applicationViewModels.queue,
                        error, cameraId, name, deviceType)
                }
            }
            if (error.errorCode<=0){
                // sucseed
                errorUpdateDeviceTextView.text = getString(R.string.update_succeed);
                context?.let {
                    errorUpdateDeviceTextView.setTextColor(ContextCompat.getColor(it, R.color.button_on))
                }


            }
            else{
                errorUpdateDeviceTextView.text = getString(R.string.update_failed);
                context?.let {
                    errorUpdateDeviceTextView.setTextColor(ContextCompat.getColor(it, R.color.button_off))
                }
            }

            editDeviceButton.isEnabled = true
        }
    }

    fun updateLocation(cameraId:Int, location: String, deviceType: DeviceType){
        editDeviceButton.isEnabled = false

        lifecycleScope.launch() {
            val error = RequestError()
            var user = applicationViewModels.dbUser

            if (user.apiKey.isNotBlank()){
                withContext(Dispatchers.IO) {
                    ApiUserRequest.updateDeviceLocation(user.apiKey, user.apiKeyDate, applicationViewModels.queue,
                        error, cameraId, location, deviceType)
                }
            }
            if (error.errorCode<=0){
                // sucseed
                errorUpdateDeviceTextView.text = getString(R.string.update_succeed);
                context?.let {
                    errorUpdateDeviceTextView.setTextColor(ContextCompat.getColor(it, R.color.button_on))
                }


            }
            else{
                errorUpdateDeviceTextView.text = getString(R.string.update_failed);
                context?.let {
                    errorUpdateDeviceTextView.setTextColor(ContextCompat.getColor(it, R.color.button_off))
                }
            }

            editDeviceButton.isEnabled = true
        }
    }

}