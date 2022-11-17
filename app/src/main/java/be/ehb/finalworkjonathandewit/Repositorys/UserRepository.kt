package be.ehb.finalworkjonathandewit.Repositorys

import androidx.annotation.WorkerThread
import androidx.room.Update
import be.ehb.finalworkjonathandewit.Daos.UserDao
import be.ehb.finalworkjonathandewit.Models.User
import kotlinx.coroutines.flow.Flow
import java.util.*

class UserRepository(private val wordDao: UserDao) {

    val allUsers: Flow<List<User>> = wordDao.getAll()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUser(userId:String):Flow<User> {
        return wordDao.getUser(userId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user:User) {
        wordDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(user:User) {
        wordDao.delete(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateKey(apiKey: String, apiKeyDate: Date, id:String) {
        wordDao.updateKey(apiKey, apiKeyDate, id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(user: User) {
        wordDao.update(user)
    }
}