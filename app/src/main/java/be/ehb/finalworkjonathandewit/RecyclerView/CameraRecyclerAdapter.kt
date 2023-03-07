package be.ehb.finalworkjonathandewit.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import be.ehb.finalworkjonathandewit.Fragments.HomeFragmentDirections
import be.ehb.finalworkjonathandewit.Models.SysStatus
import be.ehb.finalworkjonathandewit.R

class CameraRecyclerAdapter (var camera: List<SysStatus.Camera>, val navController: NavController): RecyclerView.Adapter<CameraRecyclerAdapter.CameraViewHolder>() {
    inner class CameraViewHolder(cameraItemView: View) :
        RecyclerView.ViewHolder(cameraItemView) {
        var deviceNameTextView: TextView
        var deviceTypeTextView: TextView
        var editHubButton: ImageButton
        var watchLiveButton: Button

        init {
            deviceNameTextView = cameraItemView.findViewById(R.id.cameraNameTextView)
            deviceTypeTextView = cameraItemView.findViewById(R.id.cameraTypeTextView)
            editHubButton = cameraItemView.findViewById(R.id.editCameraButton)
            watchLiveButton = cameraItemView.findViewById(R.id.watchLiveButton)
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
