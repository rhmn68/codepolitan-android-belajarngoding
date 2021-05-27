package com.codepolitan.belajarngoding.presentation.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.databinding.ActivityChangePasswordBinding
import org.jetbrains.anko.toast

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)

        onAction()
    }

    private fun onAction() {
        changePasswordBinding.apply {
            btnChangePassword.setOnClickListener {
                toast("Change Password")
            }

            btnCloseChangePassword.setOnClickListener { finish() }
        }
    }
}