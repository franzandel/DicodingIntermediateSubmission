package com.franzandel.dicodingintermediatesubmission.ui.name

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityLoginBinding
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityNameBinding

class NameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            Toast.makeText(this, "Go to Home", Toast.LENGTH_SHORT).show()
        }
    }
}
