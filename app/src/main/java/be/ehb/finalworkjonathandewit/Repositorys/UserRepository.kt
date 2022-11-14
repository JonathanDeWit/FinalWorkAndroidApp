package be.ehb.finalworkjonathandewit.Repositorys

import androidx.annotation.WorkerThread
import be.ehb.finalworkjonathandewit.Daos.UserDao
import be.ehb.finalworkjonathandewit.Models.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val wordDao: UserDao) {

    val allUsers: Flow<List<User>> = wordDao.getAll()

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
}