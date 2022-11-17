package be.ehb.finalworkjonathandewit.ViewModels

import android.content.Context
import androidx.lifecycle.*
import be.ehb.finalworkjonathandewit.Models.User
import be.ehb.finalworkjonathandewit.Repositorys.UserRepository
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch

class ApplicationViewModels(private val repository: UserRepository, context: Context) : ViewModel() {

    val queue: RequestQueue = Volley.newRequestQueue(context)
    val allUsers: LiveData<List<User>> = repository.allUsers.asLiveData()
    var dbUser = User()

    var dbUsers = 0

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        repository.delete(user)
    }

    fun updateKey(user: User) = viewModelScope.launch {
        repository.updateKey(user.apiKey, user.apiKeyDate, user.Id)
    }

    fun update(user: User) = viewModelScope.launch {
        repository.update(user)
    }
}

class UserViewModelFactory(private val repository: UserRepository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApplicationViewModels::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApplicationViewModels(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}