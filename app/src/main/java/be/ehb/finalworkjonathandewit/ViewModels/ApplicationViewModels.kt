package be.ehb.finalworkjonathandewit.ViewModels

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import be.ehb.finalworkjonathandewit.Models.User
import be.ehb.finalworkjonathandewit.Repositorys.UserRepository
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch

class ApplicationViewModels(private val repository: UserRepository, context: Context) : ViewModel() {

    val queue: RequestQueue = Volley.newRequestQueue(context)
    val allUsers: LiveData<List<User>> = repository.allUsers.asLiveData()

    var dbUsers = 0

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
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