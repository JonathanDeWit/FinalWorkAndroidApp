package be.ehb.finalworkjonathandewit.ViewModels

import androidx.lifecycle.*
import be.ehb.finalworkjonathandewit.Models.User
import be.ehb.finalworkjonathandewit.Repositorys.UserRepository
import kotlinx.coroutines.launch

class RoomViewModels(private val repository: UserRepository) : ViewModel()  {

    val allUsers: LiveData<List<User>> = repository.allUsers.asLiveData()

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModels::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModels(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}