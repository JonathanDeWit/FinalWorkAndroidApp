package be.ehb.finalworkjonathandewit.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import be.ehb.finalworkjonathandewit.Daos.UserDao
import be.ehb.finalworkjonathandewit.Models.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {


    abstract fun userDao():UserDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "securemy_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}