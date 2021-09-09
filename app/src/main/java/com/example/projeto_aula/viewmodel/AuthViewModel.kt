package com.example.projeto_aula.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projeto_aula.model.auth.AuthRepository
import com.example.projeto_aula.model.EventWrapper
import com.example.projeto_aula.model.auth.User
import com.google.firebase.auth.AuthCredential

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    enum class SignInType(val type: String) {
        Google("google"),
        Facebook("Facebook"),
        GitHub("GitHub"),
        Email("Email")
    }

    val authButtonLiveData = MutableLiveData<EventWrapper<SignInType>>()
    val authenticatedUserLiveData = MutableLiveData<User>()
    val createdLiveData = MutableLiveData<User>()

    //
    fun handleGoogleSingInClick() {
        authButtonLiveData.value = EventWrapper(SignInType.Google)
    }

    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        authRepository.firebaseSignInWithGoogle(googleAuthCredential, authenticatedUserLiveData)
    }

    fun signInWithEmail(email: String, password: String) {
        authRepository.firebaseSingInWithEmailAndPassword(
            email,
            password,
            authenticatedUserLiveData
        )
    }

    fun logInWithEmail(email: String, password: String) {
        authRepository.firebaseLogInWithEmailAndPassword(email, password, authenticatedUserLiveData)
    }

    fun createUser(user: User) {
        authRepository.createUserInFirestoreIfNotExistis(user, authenticatedUserLiveData)
    }

    class AuthViewModelFactory(private val authRepository: AuthRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AuthViewModel(authRepository) as T
        }
    }
}