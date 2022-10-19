package com.dondika.storyapp.ui.user.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.dondika.storyapp.R
import com.dondika.storyapp.data.remote.user.LoginRequest
import com.dondika.storyapp.data.remote.user.LoginResult
import com.dondika.storyapp.databinding.ActivityLoginBinding
import com.dondika.storyapp.ui.home.HomeActivity
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.UserViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupListener()
        setupObserver()

    }

    private fun setupListener(){
        binding.loginBtn.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            when {
                email.isEmpty() -> {
                    setEmailError(getString(R.string.must_filled))
                }
                password.length < 6 -> {
                    setPasswordError(getString(R.string.error_password_not_valid))
                }
                else -> {
                    val login = LoginRequest(email, password)
                    //Log.e("CEK DATA", "setupListener: ${login.email} dan ${login.password}", )
                    loginViewModel.loginRequest(login)
                }
            }
        }
    }

    private fun setupObserver(){
        loginViewModel.loginResponse.observe(this){ loginResponse ->
            when(loginResponse){
                is Result.Loading -> {

                }
                is Result.Success -> loginResponse.data?.loginResult?.let {
                    onSuccess(it)
                }
                is Result.Error -> {
                    onFailed()
                }
            }

        }
    }


    private fun onSuccess(loginResult: LoginResult){
        Toast.makeText(this, "Welcome ${loginResult.name} your token ${loginResult.token}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onFailed(){
        Snackbar.make(binding.root,"Cek your email & password", Snackbar.LENGTH_LONG).show()
    }


    private fun setEmailError(errorMsg: String?){
        binding.inputEmail.error = errorMsg
    }

    private fun setPasswordError(errorMsg: String?){
        binding.inputPassword.error = errorMsg
    }






}