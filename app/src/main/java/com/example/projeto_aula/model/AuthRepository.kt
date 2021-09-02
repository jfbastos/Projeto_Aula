package com.example.projeto_aula.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential, authenticatedUserLiveData: MutableLiveData<User>){
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener{ authTask ->
            if(authTask.isSuccessful){
                val isNewUser = authTask.result.additionalUserInfo?.isNewUser
                val currentUser = firebaseAuth.currentUser
                currentUser?.let{
                    authenticatedUserLiveData.value = currentUser.email?.let { it1 ->
                        User(currentUser.uid, currentUser.displayName,
                            it1, "",isNewUser?: false)
                    }
                }
            }

        }
    }

    fun firebaseSingInWithEmailAndPassword(email : String, password: String, authenticatedUserLiveData: MutableLiveData<User>){
        val firebaseAuth = FirebaseAuth.getInstance()
        try{
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val isNewUser = task.result.additionalUserInfo?.isNewUser
                    val currentUser = firebaseAuth.currentUser
                    currentUser?.let{
                        authenticatedUserLiveData.value = User(currentUser.uid, "", currentUser.email!!, "", isNewUser?: false)
                    }
                }else{
                    Log.d("Firebase", task.exception.toString())
                    authenticatedUserLiveData.value = null
                }
            }
        }catch (e : FirebaseAuthException) {
            Log.d("Firebase", "${e.errorCode} | ${e.message}")
        }
    }

    fun firebaseLogInWithEmailAndPassword(email: String, password: String, authenticatedUserLiveData: MutableLiveData<User>){
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val currentUser = firebaseAuth.currentUser
                currentUser?.let{
                    authenticatedUserLiveData.value = User(currentUser.uid, "", currentUser.email!!, "", isNew = false)
                }
            }else{
                authenticatedUserLiveData.value = null
            }

        }
    }

    fun createUserInFirestoreIfNotExistis(authenticatedUser : User,createdUserLiveData: MutableLiveData<User>){
        val rootRef = FirebaseFirestore.getInstance()
        val userRef = rootRef.collection(User.REF_NAME)
        val uidRef = userRef.document(authenticatedUser.uid)
        uidRef.get().addOnCompleteListener() { task ->
            if(task.isSuccessful) {
                val document = task.result
                if(!document.exists()){
                    uidRef.set(authenticatedUser).addOnCompleteListener(){
                        if(it.isSuccessful){
                            createdUserLiveData.value = authenticatedUser
                        }
                    }
                }else{
                    createdUserLiveData.value = authenticatedUser
                }
            }

        }
    }
}