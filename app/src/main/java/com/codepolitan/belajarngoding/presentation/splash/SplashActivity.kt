package com.codepolitan.belajarngoding.presentation.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.presentation.login.LoginActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        delayAndGoToLogin()
    }

    private fun delayAndGoToLogin() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                startActivity<LoginActivity>()
                finish()
            }, 1200)
    }
}