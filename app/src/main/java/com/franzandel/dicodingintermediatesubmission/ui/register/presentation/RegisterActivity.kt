package com.franzandel.dicodingintermediatesubmission.ui.register.presentation

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityRegisterBinding
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeActivity
import com.franzandel.dicodingintermediatesubmission.ui.loading.LoadingDialog
import com.franzandel.dicodingintermediatesubmission.utils.hideKeyboard
import com.franzandel.dicodingintermediatesubmission.utils.showDefaultSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    private val loadingDialog by lazy {
        LoadingDialog.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.toolbar_register)
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.nameValidation.observe(this) {
            if (it != RegisterViewModel.FORM_VALID) {
                binding.etName.error = getString(it)
            }
        }

        viewModel.usernameValidation.observe(this) {
            if (it != RegisterViewModel.FORM_VALID) {
                binding.etUsername.error = getString(it)
            }
        }

        viewModel.passwordValidation.observe(this) {
            if (it != RegisterViewModel.FORM_VALID) {
                binding.etPassword.error = getString(it)
            }
        }

        viewModel.passwordConfirmationValidation.observe(this) {
            if (it != RegisterViewModel.FORM_VALID) {
                binding.etConfirmationPassword.error = getString(it)
            }
        }

        viewModel.loading.observe(this) {
            if (it) {
                loadingDialog.show(supportFragmentManager)
            } else {
                loadingDialog.hide()
            }
        }

        viewModel.registerResult.observe(this) {
            if (it.error == null) {
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            } else {
                showDefaultSnackbar(getString(it.error), Snackbar.LENGTH_LONG)
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            etName.setOnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    viewModel.validateName(etName.text.toString())
                }
            }

            etName.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        hideKeyboard()
                        viewModel.validateName(etName.text.toString())
                        true
                    }
                    else -> false
                }
            }

            etUsername.setOnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    viewModel.validateUsername(etUsername.text.toString())
                }
            }

            etUsername.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        hideKeyboard()
                        viewModel.validateUsername(etUsername.text.toString())
                        true
                    }
                    else -> false
                }
            }

            etPassword.setOnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    viewModel.validatePassword(etPassword.text.toString())
                }
            }

            etPassword.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        hideKeyboard()
                        viewModel.validatePassword(etPassword.text.toString())
                        true
                    }
                    else -> false
                }
            }

            etConfirmationPassword.setOnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    viewModel.validateConfirmationPassword(
                        etPassword.text.toString(),
                        etConfirmationPassword.text.toString()
                    )
                }
            }

            etConfirmationPassword.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        hideKeyboard()
                        viewModel.validateConfirmationPassword(
                            etPassword.text.toString(),
                            etConfirmationPassword.text.toString()
                        )
                        true
                    }
                    else -> false
                }
            }

            btnRegister.setOnClickListener {
                viewModel.register(
                    binding.etName.text.toString(),
                    binding.etUsername.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etConfirmationPassword.text.toString()
                )
            }
        }
    }
}
