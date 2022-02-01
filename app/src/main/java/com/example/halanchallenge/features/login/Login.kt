package com.example.halanchallenge.features.login

import android.content.Context

// Call async LoginTask
class Login {
    fun login(username: String?, password: String?, context: Context) {
        val loginTask = LoginTask(context)
        loginTask.execute(username, password)
    }
}