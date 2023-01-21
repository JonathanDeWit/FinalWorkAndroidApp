package be.ehb.finalworkjonathandewit.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import be.ehb.finalworkjonathandewit.Activitys.MainActivity
import be.ehb.finalworkjonathandewit.R

class HomeFragment : Fragment(R.layout.fragment_home) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var liveVideoButton = view.findViewById<Button>(R.id.liveVideoButton)



        liveVideoButton.setOnClickListener {

            (activity as MainActivity?)?.disableNavBar()

            val action = HomeFragmentDirections.actionHomeFragmentToLiveVideoFragment2()
            findNavController().navigate(action)
        }
    }
}