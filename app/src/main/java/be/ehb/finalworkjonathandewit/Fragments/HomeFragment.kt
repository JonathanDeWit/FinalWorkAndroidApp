package be.ehb.finalworkjonathandewit.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.ehb.finalworkjonathandewit.Models.ApiUserRequest
import be.ehb.finalworkjonathandewit.Models.RequestError
import be.ehb.finalworkjonathandewit.Models.SysStatus
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.RecyclerView.CameraRecyclerAdapter
import be.ehb.finalworkjonathandewit.RecyclerView.HubRecyclerAdapter
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

    private lateinit var hubRecyclerView: RecyclerView
    private lateinit var hubAdapter: HubRecyclerAdapter
    private lateinit var cameraRecyclerView: RecyclerView
    private lateinit var cameraAdapter: CameraRecyclerAdapter
    private lateinit var errorTextView: TextView
    private lateinit var statusButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorTextView = view.findViewById<TextView>(R.id.homeErrorTextView)
        statusButton = view.findViewById<Button>(R.id.OnOffButton)
        hubRecyclerView = view.findViewById<RecyclerView>(R.id.hubRecyclerView)
        hubRecyclerView.layoutManager = LinearLayoutManager(activity)
        cameraRecyclerView = view.findViewById<RecyclerView>(R.id.cameraRecyclerView)
        cameraRecyclerView.layoutManager = LinearLayoutManager(activity)
        var reloadButton = view.findViewById<ImageButton>(R.id.reloadStatusButton)


        var sysStatus = loadStatus()


        reloadButton.setOnClickListener {
            sysStatus = loadStatus()
        }



        statusButton.setOnClickListener {
            statusButton.isEnabled = false

            sysStatus.SysState =! sysStatus.SysState
            updateButtonStatus(statusButton, sysStatus.SysState)

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
    }


    fun updateButtonStatus(button: Button, status: Boolean){
        if (status){
            button.text = getString(R.string.turn_off_the_system)
            context?.let { ContextCompat.getColor(it, R.color.button_off) }
                ?.let { button.setBackgroundColor(it) }
        }
        else{
            button.text = getString(R.string.activate_the_system)
            context?.let { ContextCompat.getColor(it, R.color.button_on) }
                ?.let { button.setBackgroundColor(it) }
        }
        statusButton.isEnabled = true
    }

    fun loadStatus():SysStatus{
        var sysStatus = SysStatus()
        statusButton.text = getString(R.string.loading)
        statusButton.isEnabled = false

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
                var hubs = sysStatus.Hubs
                var cameras = sysStatus.Cameras

                updateHubRecyclerAdapter(hubRecyclerView, hubs, cameras)
                updateCameraRecyclerAdapter(cameraRecyclerView, cameras)
            }
            else{
                errorTextView.text= getString(R.string.unable_to_get_system_state)
                statusButton.isEnabled = true
            }
        }
        return sysStatus;
    }

    fun updateHubRecyclerAdapter(hubRecyclerView:RecyclerView, hubs:List<SysStatus.Hub>,
                                 camera:List<SysStatus.Camera>){

        hubAdapter = HubRecyclerAdapter(hubs, camera, findNavController(), context, activity)
        hubRecyclerView.adapter = hubAdapter
    }
    fun updateCameraRecyclerAdapter(cameraRecyclerView:RecyclerView, camera:List<SysStatus.Camera>){
        cameraAdapter = CameraRecyclerAdapter(camera, findNavController(), context)
        cameraRecyclerView.adapter = cameraAdapter
    }
}