package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.database.model.UserEntity
import com.example.movie.viewmodel.events.LoginOrRegister
import com.example.movie.viewmodel.repositories.UserRepository
import com.example.movie.viewmodel.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val user: UserEntity? = userRepository.getSavedUsers().firstOrNull()

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
            val existingUser = userRepository.getUserByUsername(user.userName).firstOrNull()
            val existingEmail = userRepository.getUserByEmail(user.email).firstOrNull()
            if (user.password != confirmPassword){
                _state.value = _state.value?.copy( message = "Passwords do not match, please try again!")
            }
            else if (existingEmail != null) {
                _state.value = _state.value?.copy( message = "This email is already used!")
            }
            else if (existingUser == null) {
                userRepository.insertUser(user)
                userRepository.activateUser(user.userName)
                _state.value = _state.value?.copy(isSuccess = true, lastUserName = user.userName)
            } else {
                _state.value = _state.value?.copy(message = "This user already exists!")
            }
        }
    }
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByUsername(username).firstOrNull()
            if (user == null || user.password != password) {
                _state.value = _state.value?.copy(message = "This user does not exist or password is incorrect!")
            } else {
                userRepository.saveUser(username)
                userRepository.activateUser(username)
                _state.value = _state.value?.copy(isSuccess = true, lastUserName = user.userName)
            }
        }
    }
}