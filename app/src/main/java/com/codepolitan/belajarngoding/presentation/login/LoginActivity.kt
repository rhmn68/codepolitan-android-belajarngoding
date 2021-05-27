package com.codepolitan.belajarngoding.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.databinding.ActivityLoginBinding
import com.codepolitan.belajarngoding.presentation.forgotpassword.ForgotPasswordActivity
import com.codepolitan.belajarngoding.presentation.main.MainActivity
import com.codepolitan.belajarngoding.presentation.register.RegisterActivity
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        onAction()
    }

    private fun onAction() {
        loginBinding.apply {
            btnLogin.setOnClickListener {
                startActivity<MainActivity>()
            }

            btnRegister.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btnForgotPassLogin.setOnClickListener {
                startActivity<ForgotPasswordActivity>()
            }
        }
    }
}