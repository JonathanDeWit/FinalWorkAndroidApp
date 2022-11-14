package be.ehb.finalworkjonathandewit.Databases

import android.content.Context
import androidx.room.*
import be.ehb.finalworkjonathandewit.Daos.UserDao
import be.ehb.finalworkjonathandewit.Models.User

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
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
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
