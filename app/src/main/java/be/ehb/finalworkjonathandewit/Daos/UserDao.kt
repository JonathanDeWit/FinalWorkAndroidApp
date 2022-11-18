package be.ehb.finalworkjonathandewit.Daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.ehb.finalworkjonathandewit.Models.User
import kotlinx.coroutines.flow.Flow
import java.util.Date


@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user")
    fun getAllList(): List<User>

    @Query("SELECT * FROM user WHERE Id LIKE :id")
    fun getUser(id: String): Flow<User>

    @Insert
    fun insert(vararg user: User)

    @Update
    fun update(vararg user: User)

    @Query("UPDATE user SET api_key=:apiKey, api_key_date=:apiKeyDate WHERE Id LIKE :id")
    fun updateKey(apiKey: String, apiKeyDate: Date, id:String)

    @Delete
    fun delete(user:User)
}