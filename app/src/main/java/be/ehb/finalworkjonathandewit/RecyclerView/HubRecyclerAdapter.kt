package be.ehb.finalworkjonathandewit.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.ehb.finalworkjonathandewit.Models.SysStatus
import be.ehb.finalworkjonathandewit.R

class HubRecyclerAdapter(
    var hubs: List<SysStatus.Hub>,
    var cameras: List<SysStatus.Camera>,
    val navController: NavController,
    var context: Context?,
    val activity: FragmentActivity?
): RecyclerView.Adapter<HubRecyclerAdapter.HubViewHolder>() {
    inner class HubViewHolder(hubItemView: View) : RecyclerView.ViewHolder(hubItemView) {
        var hubNameTextView: TextView
        var hubTypeTextView: TextView
        var receivedCameraTextView: TextView
        var notReceivedCameraTextView: TextView
        var editHubButton: ImageButton
        var receiveCamerasRecyclerView: RecyclerView
        var notReceiveCamerasRecyclerView: RecyclerView

        init {
            hubNameTextView = hubItemView.findViewById(R.id.hubNameTextView)
            hubTypeTextView = hubItemView.findViewById(R.id.hubTypeTextView)
            receivedCameraTextView = hubItemView.findViewById(R.id.receivedCameraTextView)
            notReceivedCameraTextView = hubItemView.findViewById(R.id.notReceivedCameraTextView)
            editHubButton = hubItemView.findViewById(R.id.editHubButton)
            receiveCamerasRecyclerView = hubItemView.findViewById<RecyclerView>(R.id.receiveCamerasRecyclerView)
            receiveCamerasRecyclerView.layoutManager = LinearLayoutManager(activity)
            notReceiveCamerasRecyclerView = hubItemView.findViewById<RecyclerView>(R.id.notReceiveCamerasRecyclerView)
            notReceiveCamerasRecyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HubViewHolder {
        val monumentItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.hub_list_item, parent, false)
        return HubViewHolder(monumentItemView)
    }

    override fun onBindViewHolder(holder: HubViewHolder, position: Int) {
        val hub = hubs.get(position)
        holder.hubNameTextView.text = hub.DeviceName
        holder.hubTypeTextView.text = holder.hubTypeTextView.text.toString().plus(" Hub")
        holder.editHubButton.setOnClickListener {
            //Open Edit view
        }

        var receiveCameras = mutableListOf<String>()
        var notReceiveCameras = mutableListOf<String>()

        for (camera in cameras){
            if (camera.HubReceiveVideoStream){
                camera.DeviceName?.let { receiveCameras.add(it) }
            }
            else{
                camera.DeviceName?.let { notReceiveCameras.add(it) }
            }
        }

        if (receiveCameras.size > 0){
            val receiveCameraAdapter = CameraStatusRecyclerAdapter(receiveCameras, true, context)
            holder.receiveCamerasRecyclerView.adapter = receiveCameraAdapter
        }
        else{
            holder.receivedCameraTextView.text = ""
        }
        if(notReceiveCameras.size > 0){
            val notReceiveCameraAdapter = CameraStatusRecyclerAdapter(notReceiveCameras, false, context)
            holder.notReceiveCamerasRecyclerView.adapter = notReceiveCameraAdapter
        }
        else{
            holder.notReceivedCameraTextView.text = ""
        }






    }

    override fun getItemCount(): Int {
        return hubs.size
    }
}