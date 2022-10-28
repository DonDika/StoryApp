package com.dondika.storyapp.ui.user.login

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dondika.storyapp.R
import com.dondika.storyapp.data.remote.user.login.LoginRequest
import com.dondika.storyapp.data.remote.user.login.LoginResult
import com.dondika.storyapp.databinding.ActivityLoginBinding
import com.dondika.storyapp.ui.MainActivity
import com.dondika.storyapp.ui.user.register.RegisterActivity
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

        playAnimation()
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
                    loginViewModel.loginRequest(login)
                }
            }
        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupObserver(){
        loginViewModel.loginResponse.observe(this){ loginResponse ->
            when(loginResponse){
                is Result.Loading -> {
                    onLoading(true)
                }
                is Result.Success -> loginResponse.data?.loginResult?.let {
                    onLoading(false)
                    onSuccess(it)
                }
                is Result.Error -> {
                    onLoading(false)
                    onFailed()
                }
            }

        }
    }

    private fun onLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun onSuccess(loginResult: LoginResult){
        loginViewModel.saveUser(loginResult.token)
        Toast.makeText(this, "Welcome ${loginResult.name}!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_TOKEN, loginResult.token)
        startActivity(intent)
        finish()
    }

    private fun onFailed(){
        Snackbar.make(binding.root,getString(R.string.login_failed), Snackbar.LENGTH_LONG).show()
    }


    private fun setEmailError(errorMsg: String?){
        binding.inputEmail.error = errorMsg
    }

    private fun setPasswordError(errorMsg: String?){
        binding.inputPassword.error = errorMsg
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogo, View.TRANSLATION_X, -60f, 60f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }




}