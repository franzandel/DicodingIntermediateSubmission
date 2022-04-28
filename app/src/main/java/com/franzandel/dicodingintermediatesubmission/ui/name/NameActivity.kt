package com.franzandel.dicodingintermediatesubmission.ui.name

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityNameBinding
import com.franzandel.dicodingintermediatesubmission.utils.hideKeyboard

class NameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNameBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[NameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.nameValidation.observe(this) {
            if (it != NameViewModel.FORM_VALID) {
                binding.etName.error = getString(R.string.empty_name)
            }
        }

        viewModel.nextResult.observe(this) {
            if (it) {
                Toast.makeText(this, "Go to Home", Toast.LENGTH_SHORT).show()
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

            btnNext.setOnClickListener {
                viewModel.next(binding.etName.text.toString())
            }
        }
    }
}
