package com.example.projeto_aula.model.auth

import java.io.Serializable
import com.google.firebase.firestore.Exclude

data class User(
    var uid: String,
    var name: String? = "",
    var email: String,
    var password: String,
    @Exclude var isNew: Boolean = false
) : Serializable {
    companion object {
        val REF_NAME = "users"
    }
}