package com.religada.moviesdemo.ui.splash.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.religada.moviesdemo.R
import com.religada.moviesdemo.databinding.ActivitySplashBinding
import com.religada.moviesdemo.ui.main.activity.MainActivity
import com.religada.moviesdemo.ui.splash.viewmodel.SplashViewModel
import com.religada.moviesdemo.utils.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var prefManager: PrefManager

    private val splashViewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstTimeConfiguration()
        openMainActivity()
    }

    // First time configuration after app installation
    private fun firstTimeConfiguration() {
        if (prefManager.getBoolean(prefManager.IS_FIRST_TIME)) {
            prefManager.putBoolean(prefManager.IS_FIRST_TIME, false)
            // Do your configuration
        }
    }

    private fun openMainActivity() {
        lifecycleScope.launch {
            delay(1500)
            startActivity(Intent(baseContext, MainActivity::class.java))
            overridePendingTransition(
                R.anim.enter_right_to_left,
                R.anim.nothing
            )
            finish()
        }
    }
}
