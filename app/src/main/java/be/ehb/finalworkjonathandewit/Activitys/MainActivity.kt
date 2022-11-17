package be.ehb.finalworkjonathandewit.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import be.ehb.finalworkjonathandewit.Models.ApiUserRequest
import be.ehb.finalworkjonathandewit.Models.User
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavBar: BottomNavigationView
    private val applicationViewModels: ApplicationViewModels by viewModels {
        UserViewModelFactory((application as SecurityApplication).repository, this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        applicationViewModels.allUsers.observe(this, Observer { users ->
            Log.e("Object:", users.size.toString())
            applicationViewModels.dbUsers = users.size
            for (user in users){
                applicationViewModels.dbUser = user
                Log.e("Object:", user.Id)
            }
        })

        //https://www.youtube.com/watch?v=yLOsaR_nDrU&t=309s
        //Setup navigation en toolbar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.profileFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)


        //Set up bottom navigation bar
        bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        bottomNavBar.setupWithNavController(navController)
        disableNavBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun enableNavBar(){
        bottomNavBar.visibility = View.VISIBLE
    }
    fun disableNavBar(){
        bottomNavBar.visibility = View.GONE
    }
}