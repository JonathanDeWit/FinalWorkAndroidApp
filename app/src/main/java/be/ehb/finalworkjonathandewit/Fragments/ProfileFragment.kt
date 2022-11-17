package be.ehb.finalworkjonathandewit.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.ehb.finalworkjonathandewit.Models.User
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import be.ehb.finalworkjonathandewit.Activitys.MainActivity
import be.ehb.finalworkjonathandewit.Models.ApiUserRequest
import be.ehb.finalworkjonathandewit.Models.LoginUser
import be.ehb.finalworkjonathandewit.Models.RequestError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val applicationViewModels: ApplicationViewModels by activityViewModels {
        UserViewModelFactory((activity?.application as SecurityApplication).repository, requireActivity())
    }

    private var appUser = User()

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
                    appUser = user
                    firstNameTextView.text = user.UserName
                    firstNameTextView.text = getString(R.string.first_name).plus(" ").plus(user.FirstName)
                    lastNameTextView.text = getString(R.string.last_name).plus(" ").plus(user.LastName)
                    emailTextView.text = getString(R.string.email).plus(" ").plus(user.Email)

                    Log.e("Object:", user.UserName)
                }
            })
        }
        Log.e("User", appUser.Id)

        deleteButton.setOnClickListener {
            if (appUser.UserName.isNotBlank()){
                deleteAccountAlertDialog()
            }
        }



    }

    fun deleteAccountAlertDialog(){
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater;
        val layout = inflater.inflate(R.layout.alertdialog_delete_profile, null)

        with(builder){
            setPositiveButton(getString(R.string.delete_profile)){ dialog, id ->
                val password = layout.findViewById<EditText>(R.id.enterPasswordEditText).text
                lifecycleScope.launch {
                    val error = RequestError()
                    var deleteStatus = false

                    withContext(Dispatchers.IO) {
                        deleteStatus = ApiUserRequest.deleteUser(applicationViewModels.queue, error, LoginUser(appUser.UserName, password.toString()),
                            appUser.apiKey, appUser.apiKeyDate)
                    }

                    Log.e("Error code", error.errorCode.toString())
                    if (deleteStatus){
                        activity?.let {
                            applicationViewModels.allUsers.observe(it, Observer { users ->
                                applicationViewModels.dbUsers = users.size
                                for (user in users){
                                    applicationViewModels.delete(user)
                                }
                            })
                        }
                    }

                    withContext(Dispatchers.Main) {
                        if (deleteStatus){
                            Toast.makeText(activity, getString(R.string.user_is_deleted), Toast.LENGTH_SHORT).show()
                            startLogin()
                        }else{
                            Toast.makeText(activity, getString(R.string.error_user_is_deleted), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            setNegativeButton(getString(R.string.cansel)) { dialog, id ->

            }
            setView(layout)
            show()
        }
    }

    fun startLogin(){
        (activity as MainActivity?)?.disableNavBar()
        val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
        findNavController().navigate(action)

    }
}