package com.franzandel.dicodingintermediatesubmission.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityLoginBinding
import com.franzandel.dicodingintermediatesubmission.ui.name.NameActivity
import com.franzandel.dicodingintermediatesubmission.utils.hideKeyboard

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
        initObserver()
        initListener()
    }

    private fun initObserver() {
        binding.apply {
            loginViewModel.usernameValidation.observe(this@LoginActivity) {
                if (it != LoginViewModel.FORM_VALID) {
                    etUsername.error = getString(it)
                } else {
                    if (loginViewModel.passwordValidation.value == LoginViewModel.FORM_VALID) {
                        pbLoading.visibility = View.VISIBLE
                        loginViewModel.login(etUsername.text.toString(), etPassword.text.toString())
                    }
                }
            }

            loginViewModel.passwordValidation.observe(this@LoginActivity) {
                if (it != LoginViewModel.FORM_VALID) {
                    etPassword.error = getString(it)
                } else {
                    if (loginViewModel.usernameValidation.value == LoginViewModel.FORM_VALID) {
                        pbLoading.visibility = View.VISIBLE
                        loginViewModel.login(etUsername.text.toString(), etPassword.text.toString())
                    }
                }
            }

            loginViewModel.loginResult.observe(this@LoginActivity, Observer {
                val loginResult = it ?: return@Observer

                pbLoading.visibility = View.GONE
                if (loginResult.error != null) {
                    showLoginFailed(loginResult.error)
                }
                if (loginResult.success != null) {
                    updateUiWithUser(loginResult.success)
                    startActivity(Intent(this@LoginActivity, NameActivity::class.java))
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

            btnNext.setOnClickListener {
                loginViewModel.validateUsername(etUsername.text.toString())
                loginViewModel.validatePassword(etPassword.text.toString())
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
