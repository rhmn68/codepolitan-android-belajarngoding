package com.codepolitan.belajarngoding.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.databinding.ActivityRegisterBinding
import com.codepolitan.belajarngoding.model.User
import com.codepolitan.belajarngoding.utils.hideSoftKeyboard
import com.codepolitan.belajarngoding.utils.showDialogError
import com.codepolitan.belajarngoding.utils.showDialogLoading
import com.codepolitan.belajarngoding.utils.showDialogSuccess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userDatabase: DatabaseReference
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        //Init
        firebaseAuth = FirebaseAuth.getInstance()
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        dialogLoading = showDialogLoading(this)

        onAction()
    }

    private fun onAction() {
        registerBinding.apply {
            btnCloseRegister.setOnClickListener {
                finish()
            }

            btnRegister.setOnClickListener {
                val name = etNameRegister.text.toString().trim()
                val email = etEmailRegister.text.toString().trim()
                val pass = etPasswordRegister.text.toString().trim()
                val confirmPass = etConfirmPasswordRegister.text.toString().trim()

                if (checkValidation(name, email, pass, confirmPass)){
                    hideSoftKeyboard(this@RegisterActivity, registerBinding.root)
                    registerToServer(name, email, pass)
                }
            }
        }
    }

    private fun registerToServer(name: String, email: String, pass: String) {
        dialogLoading.show()
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                val uid = it.user?.uid
                val imageUrl = "https://ui-avatars.com/api/?background=218B5E&color=fff&size=100&rounded=true&name=$name"
                val user = User(
                    uidUser = uid,
                    nameUser = name,
                    emailUser = email,
                    avatarUser = imageUrl
                )
                userDatabase
                    .child(uid.toString())
                    .setValue(user)
                    .addOnSuccessListener {
                        dialogLoading.dismiss()
                        val dialogSuccess = showDialogSuccess(this, getString(R.string.success_register))
                        dialogSuccess.show()

                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                dialogSuccess.dismiss()
                                finish()
                            }, 1500)
                    }
                    .addOnFailureListener {
                        dialogLoading.dismiss()
                        showDialogError(this, it.message.toString())
                    }
            }
            .addOnFailureListener {
                dialogLoading.dismiss()
                showDialogError(this, it.message.toString())
            }
    }

    private fun checkValidation(
        name: String,
        email: String,
        pass: String,
        confirmPass: String
    ): Boolean {
        registerBinding.apply {
            when{
                name.isEmpty() -> {
                    etNameRegister.error = getString(R.string.please_field_your_name)
                    etNameRegister.requestFocus()
                }
                email.isEmpty() -> {
                    etEmailRegister.error = getString(R.string.please_field_your_email)
                    etEmailRegister.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmailRegister.error = getString(R.string.please_use_valid_email)
                    etEmailRegister.requestFocus()
                }
                pass.isEmpty() -> {
                    etPasswordRegister.error = getString(R.string.please_field_your_password)
                    etPasswordRegister.requestFocus()
                }
                pass.length < 8 -> {
                    etPasswordRegister.error = getString(R.string.please_field_your_password_more_than_8)
                    etPasswordRegister.requestFocus()
                }
                confirmPass.isEmpty() -> {
                    etConfirmPasswordRegister.error = getString(R.string.please_field_your_confirm_password)
                    etConfirmPasswordRegister.requestFocus()
                }
                confirmPass.length < 8 -> {
                    etConfirmPasswordRegister.error = getString(R.string.please_field_your_confirm_password_more_than_8)
                    etConfirmPasswordRegister.requestFocus()
                }
                pass != confirmPass -> {
                    etPasswordRegister.error = getString(R.string.your_password_didnt_match)
                    etPasswordRegister.requestFocus()
                    etConfirmPasswordRegister.error = getString(R.string.your_password_didnt_match)
                    etConfirmPasswordRegister.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}