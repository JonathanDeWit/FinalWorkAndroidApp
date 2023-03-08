package be.ehb.finalworkjonathandewit.RecyclerView

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import be.ehb.finalworkjonathandewit.Fragments.HomeFragmentDirections
import be.ehb.finalworkjonathandewit.Models.SysStatus
import be.ehb.finalworkjonathandewit.R

class CameraStatusRecyclerAdapter(
    var cameraName: List<String>,
    val received: Boolean,
    val context: Context?
    ) : RecyclerView.Adapter<CameraStatusRecyclerAdapter.CameraViewHolder>() {
    inner class CameraViewHolder(cameraItemView: View) :
        RecyclerView.ViewHolder(cameraItemView) {
        var cameraTransmitionStatusTextView: TextView

        init {
            cameraTransmitionStatusTextView = cameraItemView.findViewById(R.id.cameraStatusNameTextView)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val cameraItemView = LayoutInflater.from(parent.context).inflate(R.layout.camera_status_list_item, parent, false)
        return CameraViewHolder(cameraItemView)
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
        val camera = cameraName.get(position)

        if (received){
            holder.cameraTransmitionStatusTextView.text = camera
            context?.let { ContextCompat.getColor(it, R.color.button_on) }
                ?.let { holder.cameraTransmitionStatusTextView.setTextColor(it) }
        }
        else{
            holder.cameraTransmitionStatusTextView.text = camera
            context?.let { ContextCompat.getColor(it, R.color.button_off) }
                ?.let { holder.cameraTransmitionStatusTextView.setTextColor(it) }
        }

    }
    override fun getItemCount(): Int {
        return cameraName.size
    }
}