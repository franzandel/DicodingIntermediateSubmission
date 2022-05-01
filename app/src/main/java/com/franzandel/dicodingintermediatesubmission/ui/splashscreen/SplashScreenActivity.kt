package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.franzandel.dicodingintermediatesubmission.R

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

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
            delayOneSecond()
        } else {
            splashScreen?.setKeepOnScreenCondition { true }
        }
    }

    private fun isBelowAndroid12(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.R

    private fun delayOneSecond() {
        Handler(Looper.getMainLooper()).postDelayed({
            kotlin.run {
                // Go to specific page
                finish()
            }
        }, DELAY_ONE_SECOND)
    }

    companion object {
        private const val DELAY_ONE_SECOND = 1000L
    }
}
