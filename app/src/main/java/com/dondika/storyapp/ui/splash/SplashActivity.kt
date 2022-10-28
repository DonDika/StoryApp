package com.dondika.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dondika.storyapp.ui.MainActivity
import com.dondika.storyapp.ui.user.login.LoginActivity
import com.dondika.storyapp.utils.UserViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validateUser()
    }

    private fun validateUser() {
        splashViewModel.fetchUser().observe(this){ token ->
            if (token != ""){
                startActivity(Intent(this@SplashActivity, MainActivity::class.java)
                    .putExtra(MainActivity.EXTRA_TOKEN, token))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

}