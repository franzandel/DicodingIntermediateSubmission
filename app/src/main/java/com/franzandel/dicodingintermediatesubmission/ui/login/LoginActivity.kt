package com.franzandel.dicodingintermediatesubmission.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityLoginBinding
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import com.franzandel.dicodingintermediatesubmission.ui.addstory.AddStoryActivity
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeActivity
import com.franzandel.dicodingintermediatesubmission.ui.loading.LoadingDialog
import com.franzandel.dicodingintermediatesubmission.ui.register.presentation.RegisterActivity
import com.franzandel.dicodingintermediatesubmission.utils.extension.showDefaultSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    private val loadingDialog by lazy {
        LoadingDialog.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.toolbar_login)
        initObserver()
        initListener()
    }

    private fun initObserver() {
        binding.apply {
            loginViewModel.loading.observe(this@LoginActivity) {
                if (it) {
                    loadingDialog.show(supportFragmentManager)
                } else {
                    loadingDialog.hide()
                }
            }

            loginViewModel.loginResult.observe(this@LoginActivity, Observer {
                val loginResult = it ?: return@Observer

                if (loginResult.error != null) {
                    showLoginFailed(loginResult.error)
                }
                if (loginResult.success != null) {
                    startActivity(HomeActivity.newIntent(this@LoginActivity))
                    finishAffinity()
                }
                EspressoIdlingResource.decrement()
            })
        }
    }

    private fun initListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                etUsername.validateUsername(username)
                etPassword.validatePassword(password)
                if (etUsername.error == null && etUsername.error == null) {
                    EspressoIdlingResource.increment()
                    val loginRequest = LoginRequest(
                        email = username,
                        password = password
                    )
                    loginViewModel.login(loginRequest)
                }
            }

            tvNoAccountYet.setOnClickListener {
                startActivity(
                    RegisterActivity.newIntent(this@LoginActivity),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity)
                        .toBundle()
                )
            }
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        showDefaultSnackbar(getString(errorString), Snackbar.LENGTH_SHORT)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
