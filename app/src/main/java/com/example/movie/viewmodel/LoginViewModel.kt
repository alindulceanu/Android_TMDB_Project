package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.database.model.UserEntity
import com.example.movie.viewmodel.events.LoginOrRegister
import com.example.movie.viewmodel.repositories.LoginRepository
import com.example.movie.viewmodel.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val user: UserEntity? = repository.getSavedUsers().firstOrNull()

            if (user != null) {
                login(user.userName, user.password)
            }
            _state.value = _state.value?.copy(isVerifying = false)
        }
    }

    private val _state = MutableLiveData<UserState>().apply { value = UserState() }

    val state: LiveData<UserState> get() = _state

    fun setState (newState: LoginOrRegister) {
        _state.value = _state.value?.copy(loginOrRegister = newState)
    }

    fun register(user: UserEntity, confirmPassword: String) {
        viewModelScope.launch {
            val existingUser = repository.getUserByUsername(user.userName).firstOrNull()
            if (user.password != confirmPassword){
                _state.value = _state.value?.copy( message = "Passwords do not match, please try again!")
            }
            else if (existingUser == null) {
                repository.insertUser(user)
                _state.value = _state.value?.copy(isSuccess = true, lastUserName = user.userName)
            } else {
                _state.value = _state.value?.copy(message = "This user already exists!")
            }
        }
    }
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username).firstOrNull()
            if (user == null || user.password != password) {
                _state.value = _state.value?.copy(message = "This user was not found!")
            } else {
                repository.saveUser(user.userName)
                _state.value = _state.value?.copy(isSuccess = true, lastUserName = user.userName)
            }
        }
    }
}