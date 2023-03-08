package be.ehb.finalworkjonathandewit.RecyclerView

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import be.ehb.finalworkjonathandewit.Fragments.HomeFragmentDirections
import be.ehb.finalworkjonathandewit.Models.SysStatus
import be.ehb.finalworkjonathandewit.R

class CameraRecyclerAdapter(
    var camera: List<SysStatus.Camera>,
    val navController: NavController,
    var context: Context?
): RecyclerView.Adapter<CameraRecyclerAdapter.CameraViewHolder>() {
    inner class CameraViewHolder(cameraItemView: View) :
        RecyclerView.ViewHolder(cameraItemView) {
        var deviceNameTextView: TextView
        var deviceTypeTextView: TextView
        var cameraTranStatusTextView: TextView
        var cameraLocationTextView: TextView
        var editHubButton: ImageButton
        var watchLiveButton: Button

        init {
            deviceNameTextView = cameraItemView.findViewById(R.id.CameraNameTextView)
            deviceTypeTextView = cameraItemView.findViewById(R.id.cameraTypeTextView)
            editHubButton = cameraItemView.findViewById(R.id.editCameraButton)
            watchLiveButton = cameraItemView.findViewById(R.id.watchLiveButton)
            cameraLocationTextView = cameraItemView.findViewById(R.id.cameraLocationTextView)
            cameraTranStatusTextView = cameraItemView.findViewById(R.id.cameraTranStatusTextView)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val cameraItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.camera_list_item, parent, false)
        return CameraViewHolder(cameraItemView)
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
        val camera = camera.get(position)
        holder.deviceNameTextView.text = camera.DeviceName
        holder.deviceTypeTextView.text = holder.deviceTypeTextView.text.toString().plus(" Camera")
        holder.editHubButton.setOnClickListener {
            //Open Edit view
        }

        //Check if the live video is send by the camera and converted by the hub
        if (camera.TransmitVideoStream){
            holder.cameraTranStatusTextView.text = context?.getString(R.string.camera_is_transmitting)
            context?.let { ContextCompat.getColor(it, R.color.button_on) }
                ?.let { holder.cameraTranStatusTextView.setTextColor(it) }

            if (camera.HubReceiveVideoStream){
                holder.watchLiveButton.isEnabled = true;

                context?.let { ContextCompat.getColor(it, R.color.button_off) }
                    ?.let { holder.watchLiveButton.setBackgroundColor(it) }
                holder.watchLiveButton.text = context?.getString(R.string.watch_live)
            }
            else{
                holder.watchLiveButton.isEnabled = false;
                holder.watchLiveButton.setBackgroundColor(Color.parseColor("#A5A5A5"))
                holder.watchLiveButton.text = context?.getString(R.string.camera_not_online)
            }
        }
        else{
            holder.cameraTranStatusTextView.text = context?.getString(R.string.camera_is_not_transmitting)
            context?.let { ContextCompat.getColor(it, R.color.button_off) }
                ?.let { holder.cameraTranStatusTextView.setTextColor(it) }

            holder.watchLiveButton.isEnabled = false;
            holder.watchLiveButton.setBackgroundColor(Color.parseColor("#A5A5A5"))
            holder.watchLiveButton.text = context?.getString(R.string.camera_not_online)
        }



       //Check if device has a location

        if (camera.DeviceLocation != null){
            holder.cameraLocationTextView.text = context?.getString(R.string.device_location).plus(camera.DeviceLocation)
        }
        else{
            holder.cameraLocationTextView.text = ""
        }


        holder.watchLiveButton.setOnClickListener {
            val action = camera.VideoStream?.OutgoingStreamPort?.let { it1 ->
                camera.VideoStream?.SrtServerIp?.let { it2 ->
                    HomeFragmentDirections.actionHomeFragmentToLiveVideoFragment2(
                        it2,
                        it1
                    )
                }
            }
            if (action != null) {
                navController.navigate(action)
            }
        }
    }
    override fun getItemCount(): Int {
        return camera.size
    }
}
