package com.example.movie.viewmodel.state

import com.example.movie.viewmodel.events.LoginOrRegister

data class UserState (
    val isVerifying: Boolean = true,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val loginOrRegister: LoginOrRegister = LoginOrRegister.LOGIN,
    var lastUserName: String? = null,
)