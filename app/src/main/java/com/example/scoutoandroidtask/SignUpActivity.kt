package com.example.scoutoandroidtask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.scoutoandroidtask.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.signUpBtn.setOnClickListener {
            val name = binding.nameEdt.text.toString()
            val email = binding.emailEdt.text.toString()
            val password = binding.passwordEdt.text.toString()
            val confirmPassword = binding.confirmPasswordEdt.text.toString()
            if (name.isEmpty()) binding.nameEdt.error = "Please enter name"
            if (email.isEmpty()) binding.emailEdt.error = "Please enter email"
            if (password.isEmpty()) binding.passwordEdt.error = "Please enter password"
            if (password != confirmPassword) binding.passwordEdt.error =
                "Please enter same password"
            if (isValidPassword(password)) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Shared Preference
                            val pref = getSharedPreferences("login", MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putBoolean("flag", true)
                            editor.apply()
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            } else {
                binding.passwordEdt.error = "Password must contain at least 8 characters, including UPPER/lowercase, special character and numbers "
            }
        }
    }

    // Password Validation Code
    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.firstOrNull { it.isDigit() } == null) return false
        if (password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }
                .firstOrNull() == null) return false
        if (password.firstOrNull { !it.isLetterOrDigit() } == null) return false
        return true
    }
}