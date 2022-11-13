package be.ehb.finalworkjonathandewit

import android.app.Application
import be.ehb.finalworkjonathandewit.Databases.AppDatabase
import be.ehb.finalworkjonathandewit.Repositorys.UserRepository

class SecurityApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { UserRepository(database.userDao()) }
}