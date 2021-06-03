package com.codepolitan.belajarngoding.presentation.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import com.bumptech.glide.Glide
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.databinding.ActivityUserBinding
import com.codepolitan.belajarngoding.model.User
import com.codepolitan.belajarngoding.presentation.changepassword.ChangePasswordActivity
import com.codepolitan.belajarngoding.presentation.login.LoginActivity
import com.codepolitan.belajarngoding.utils.showDialogError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.jetbrains.anko.startActivity

class UserActivity : AppCompatActivity() {

    private lateinit var userBinding: ActivityUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userDatabase: DatabaseReference
    private var currentUser: FirebaseUser? = null

    private var listenerUser = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            val user = snapshot.getValue(User::class.java)
            user?.let {
                userBinding.tvNameUser.text = it.nameUser
                userBinding.tvEmailUser.text = it.emailUser

                Glide
                    .with(this@UserActivity)
                    .load(it.avatarUser)
                    .placeholder(android.R.color.darker_gray)
                    .into(userBinding.ivAvatarUser)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            showDialogError(this@UserActivity, error.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        //Init
        firebaseAuth = FirebaseAuth.getInstance()
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        currentUser = firebaseAuth.currentUser

        getDataFirebase()
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

            swipeUser.setOnRefreshListener {
                getDataFirebase()
            }
        }
    }

    private fun getDataFirebase() {
        showLoading()
        userDatabase
            .child(currentUser?.uid.toString())
            .addValueEventListener(listenerUser)
    }

    private fun showLoading(){
        userBinding.swipeUser.isRefreshing = true
    }

    private fun hideLoading(){
        userBinding.swipeUser.isRefreshing = false
    }
}