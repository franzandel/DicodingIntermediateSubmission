package com.franzandel.dicodingintermediatesubmission.ui.register.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.data.consts.ValidationConst
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityRegisterBinding
import com.franzandel.dicodingintermediatesubmission.ui.addstory.AddStoryActivity
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeActivity
import com.franzandel.dicodingintermediatesubmission.ui.loading.LoadingDialog
import com.franzandel.dicodingintermediatesubmission.utils.extension.hideKeyboard
import com.franzandel.dicodingintermediatesubmission.utils.extension.showDefaultSnackbar
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
        viewModel.passwordConfirmationValidation.observe(this) {
            if (it != ValidationConst.FORM_VALID) {
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
                startActivity(HomeActivity.newIntent(this))
                finishAffinity()
            } else {
                showDefaultSnackbar(getString(it.error), Snackbar.LENGTH_LONG)
            }
        }
    }

    private fun initListeners() {
        binding.apply {
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
                val registerRequest = RegisterRequest(
                    name = binding.etName.text.toString(),
                    email = binding.etUsername.text.toString(),
                    password = binding.etPassword.text.toString()
                )
                viewModel.register(
                    registerRequest,
                    binding.etName.error == null,
                    binding.etUsername.error == null,
                    binding.etPassword.error == null,
                    binding.etConfirmationPassword.text.toString()
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}
