package com.franzandel.dicodingintermediatesubmission.ui.register.presentation

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityRegisterBinding
import com.franzandel.dicodingintermediatesubmission.utils.hideKeyboard

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by lazy {
        ViewModelProvider(this, RegisterViewModelFactory())[RegisterViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

        viewModel.registerResult.observe(this) {
            if (it.error == null) {
                Toast.makeText(this, "Go to Home", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(it.error), Toast.LENGTH_SHORT).show()
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
