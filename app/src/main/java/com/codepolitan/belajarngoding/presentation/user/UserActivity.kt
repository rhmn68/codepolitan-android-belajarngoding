package com.codepolitan.belajarngoding.presentation.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.databinding.ActivityUserBinding
import com.codepolitan.belajarngoding.presentation.changepassword.ChangePasswordActivity
import com.codepolitan.belajarngoding.presentation.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class UserActivity : AppCompatActivity() {

    private lateinit var userBinding: ActivityUserBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        //Init
        firebaseAuth = FirebaseAuth.getInstance()

        onAction()
    }

    private fun onAction() {
        userBinding.apply {
            btnCloseUser.setOnClickListener { finish() }

            btnChangeLanguageUser.setOnClickListener {
                startActivity(Intent(ACTION_LOCALE_SETTINGS))
            }

            btnChangePasswordUser.setOnClickListener {
                startActivity<ChangePasswordActivity>()
            }

            btnLogoutUser.setOnClickListener {
                firebaseAuth.signOut()
                startActivity<LoginActivity>()
                finishAffinity()
            }
        }
    }
}