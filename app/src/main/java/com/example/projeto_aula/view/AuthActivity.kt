package com.example.projeto_aula.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.projeto_aula.R
import com.example.projeto_aula.databinding.ActivityAuthBinding
import com.example.projeto_aula.model.auth.AuthRepository
import com.example.projeto_aula.model.auth.User
import com.example.projeto_aula.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInResultLaucher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        authViewModel =
            ViewModelProvider(this, AuthViewModel.AuthViewModelFactory(AuthRepository())).get(
                AuthViewModel::class.java
            )

        initRegistersAndObservers()
    }

    private fun initRegistersAndObservers() {
        binding.btnCadastrar.setOnClickListener {
            binding.textinputError.visibility = View.INVISIBLE
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                authViewModel.signInWithEmail(email, password)
            }

            authViewModel.authenticatedUserLiveData.observe(this) { user ->
                if (user != null) {
                    goToMainActivity(user)
                } else {
                    binding.textinputError.text = "Usuário já existe"
                    binding.textinputError.visibility = View.VISIBLE
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            binding.textinputError.visibility = View.INVISIBLE
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                authViewModel.logInWithEmail(email, password)
            }

            authViewModel.authenticatedUserLiveData.observe(this) { user ->
                if (user != null) {

                    goToMainActivity(user)
                } else {
                    binding.textinputError.text = "Usuario/senha inválido"
                    binding.textinputError.visibility = View.VISIBLE
                }
            }
        }

        binding.btnGoogle.setOnClickListener {
            authViewModel.handleGoogleSingInClick()
        }

        authViewModel.authButtonLiveData.observe(this) { signInType ->
            signInType.getContentIfNotHandled()?.let { type ->
                when (type) {
                    AuthViewModel.SignInType.Google -> signInWithGoogle()
                    else -> Log.d("login type", "Login type not found")
                }
            }
        }

        authViewModel.authenticatedUserLiveData.observe(this) { user ->
            if (user.isNew) {
                authViewModel.createUser(user)
            } else {
                goToMainActivity(user)
            }
        }


        authViewModel.createdLiveData.observe(this) { user ->
            goToMainActivity(user)
        }

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.oauth_token))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        signInResultLaucher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val googleAccount = task.result
                    if (googleAccount != null) {
                        getGoogleAuthCredential(googleAccount)
                    }
                }
            }

    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this, BuscaCepActivity::class.java)
        intent.putExtra(BuscaCepActivity.USER_EXTRA, user)
        startActivity(intent)
    }

    private fun getGoogleAuthCredential(googleAccount: GoogleSignInAccount) {
        val googleTokenId = googleAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        authViewModel.signInWithGoogle(googleAuthCredential)
    }

    private fun signInWithGoogle() {
        signInResultLaucher.launch(googleSignInClient.signInIntent)
    }
}