package be.ehb.finalworkjonathandewit.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.ehb.finalworkjonathandewit.Models.User
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import androidx.lifecycle.Observer

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val applicationViewModels: ApplicationViewModels by activityViewModels {
        UserViewModelFactory((activity?.application as SecurityApplication).repository, requireActivity())
    }

    private var user = User()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var firstNameTextView = view.findViewById<TextView>(R.id.firstNameTextView)
        var lastNameTextView = view.findViewById<TextView>(R.id.lastNameTextView)
        var emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        var deleteButton = view.findViewById<Button>(R.id.deleteButton)


        activity?.let {
            applicationViewModels.allUsers.observe(it, Observer { users ->
                applicationViewModels.dbUsers = users.size
                for (user in users){
                    firstNameTextView.text = user.UserName
                    firstNameTextView.text = getString(R.string.first_name).plus(" ").plus(user.FirstName)
                    lastNameTextView.text = getString(R.string.last_name).plus(" ").plus(user.LastName)
                    emailTextView.text = getString(R.string.email).plus(" ").plus(user.Email)

                    Log.e("Object:", user.UserName)
                }
            })
        }

        deleteButton.setOnClickListener {

        }



    }
}