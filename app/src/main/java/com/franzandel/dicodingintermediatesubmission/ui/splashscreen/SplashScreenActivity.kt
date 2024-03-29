package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeActivity
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        var splashScreen: SplashScreen? = null
        if (isBelowAndroid12()) {
            setStatusAndNavigationBarOverlap()
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
        EspressoIdlingResource.increment()
    }

    private fun setStatusAndNavigationBarOverlap() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
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

                startActivity(
                    Intent(this, clazz).apply {
                        action = intent.action
                        intent.extras?.let { bundle ->
                            putExtras(bundle)
                        }
                    }
                )
                finish()
                EspressoIdlingResource.decrement()
            }
        }, DELAY_ONE_SECOND)
    }

    companion object {
        private const val DELAY_ONE_SECOND = 1000L
    }
}
