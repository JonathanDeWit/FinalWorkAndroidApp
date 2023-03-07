package be.ehb.finalworkjonathandewit.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import be.ehb.finalworkjonathandewit.Models.SysStatus
import be.ehb.finalworkjonathandewit.R

class HubRecyclerAdapter (var hubs: List<SysStatus.Hub>): RecyclerView.Adapter<HubRecyclerAdapter.HubViewHolder>() {
    inner class HubViewHolder(hubItemView: View) : RecyclerView.ViewHolder(hubItemView) {
        var hubNameTextView: TextView
        var hubTypeTextView: TextView
        var editHubButton: ImageButton

        init {
            hubNameTextView = hubItemView.findViewById(R.id.hubNameTextView)
            hubTypeTextView = hubItemView.findViewById(R.id.hubTypeTextView)
            editHubButton = hubItemView.findViewById(R.id.editHubButton)
        }
    }

    var context: Context? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
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
//            val intent = Intent(context, MonumentActivity::class.java)
//            intent.putExtra(MainActivity.EXTRA_MONUMENT_API_ID, monument.apiId)
//            context?.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return hubs.size
    }
}