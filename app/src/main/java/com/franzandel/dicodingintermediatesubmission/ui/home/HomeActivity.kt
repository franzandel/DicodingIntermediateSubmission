package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityHomeBinding
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityLoginBinding
import com.franzandel.dicodingintermediatesubmission.ui.splashscreen.SplashScreenViewModelFactory
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val adapter by lazy { HomeAdapter() }
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(applicationContext) }
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRV()
        initObservers()
        viewModel.getStories()
    }

    private fun initRV() {
        binding.rvHome.adapter = this@HomeActivity.adapter
    }

    private fun initObservers() {
        viewModel.homeResult.observe(this) {
            if (it.success != null) {
                lifecycleScope.launch(coroutineThread.background) {
                    adapter.submitData(it.success)
                }
            }
            if (it.error != null) {
                // Show error page
            }
        }
    }
}
