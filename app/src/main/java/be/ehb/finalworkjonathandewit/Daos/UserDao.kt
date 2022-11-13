package be.ehb.finalworkjonathandewit.Daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import be.ehb.finalworkjonathandewit.Models.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Insert
    fun insert(vararg user: User)

    @Delete
    fun delete(user:User)
}