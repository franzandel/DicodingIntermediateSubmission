package com.franzandel.dicodingintermediatesubmission.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityLoginBinding
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeActivity
import com.franzandel.dicodingintermediatesubmission.ui.loading.LoadingDialog
import com.franzandel.dicodingintermediatesubmission.ui.register.presentation.RegisterActivity
import com.franzandel.dicodingintermediatesubmission.utils.hideKeyboard

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    private val loadingDialog by lazy {
        LoadingDialog.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory(applicationContext))[LoginViewModel::class.java]
        initObserver()
        initListener()
    }

    private fun initObserver() {
        binding.apply {
            loginViewModel.usernameValidation.observe(this@LoginActivity) {
                if (it != LoginViewModel.FORM_VALID) {
                    etUsername.error = getString(it)
                }
            }

            loginViewModel.passwordValidation.observe(this@LoginActivity) {
                if (it != LoginViewModel.FORM_VALID) {
                    etPassword.error = getString(it)
                }
            }

            loginViewModel.loadingVisibility.observe(this@LoginActivity) {
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
                    updateUiWithUser(loginResult.success)
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                }
            })
        }
    }

    private fun initListener() {
        binding.apply {
            etUsername.setOnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    loginViewModel.validateUsername(etUsername.text.toString())
                }
            }

            etUsername.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_NEXT -> {
                        hideKeyboard()
                        loginViewModel.validateUsername(etUsername.text.toString())
                        true
                    }
                    else -> false
                }
            }

            etPassword.setOnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    loginViewModel.validatePassword(etPassword.text.toString())
                }
            }

            etPassword.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        hideKeyboard()
                        loginViewModel.validatePassword(etPassword.text.toString())
                        true
                    }
                    else -> false
                }
            }

            btnLogin.setOnClickListener {
                loginViewModel.login(etUsername.text.toString(), etPassword.text.toString())
            }

            tvNoAccountYet.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
