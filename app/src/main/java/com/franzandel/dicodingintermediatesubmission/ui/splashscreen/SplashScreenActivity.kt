package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeActivity
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginActivity
import com.franzandel.dicodingintermediatesubmission.ui.register.presentation.RegisterActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SplashScreenViewModelFactory(applicationContext)
        )[SplashScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var splashScreen: SplashScreen? = null
        if (isBelowAndroid12()) {
            setTheme(R.style.Theme_App_StartingBelowAPI12)
        } else {
            splashScreen = installSplashScreen()
        }
        super.onCreate(savedInstanceState)

        if (isBelowAndroid12()) {
            setContentView(R.layout.activity_splash_screen)
        } else {
            splashScreen?.setKeepOnScreenCondition { true }
        }
        initObservers()
        viewModel.getToken()
    }

    private fun initObservers() {
        viewModel.isTokenEmpty.observe(this) {
            delayOneSecond(it)
        }
    }

    private fun isBelowAndroid12(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.R

    private fun delayOneSecond(isTokenEmpty: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            kotlin.run {
                val clazz = if (isTokenEmpty) {
                    LoginActivity::class.java
                } else {
                    HomeActivity::class.java
                }

                startActivity(Intent(this, clazz))
                finish()
            }
        }, DELAY_ONE_SECOND)
    }

    companion object {
        private const val DELAY_ONE_SECOND = 1000L
    }
}
