package be.ehb.finalworkjonathandewit.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import be.ehb.finalworkjonathandewit.R
import be.ehb.finalworkjonathandewit.SecurityApplication
import be.ehb.finalworkjonathandewit.ViewModels.ApplicationViewModels
import be.ehb.finalworkjonathandewit.ViewModels.RoomViewModels
import be.ehb.finalworkjonathandewit.ViewModels.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavBar: BottomNavigationView
    private val roomViewModel: RoomViewModels by viewModels {
        UserViewModelFactory((application as SecurityApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roomViewModel.allUsers

        val applicationViewModels: ApplicationViewModels by viewModels()


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
        bottomNavBar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    public fun enableNavBar(){
        bottomNavBar.visibility = View.VISIBLE
    }
}