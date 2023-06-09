package com.example.scoutoandroidtask

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.scoutoandroidtask.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var binding : ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.signUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        // Shared Preference Code
        val pref = getSharedPreferences("login", MODE_PRIVATE)
        val check = pref.getBoolean("flag", false)
        if (check) {
            // for true Login (User already login )
            startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
            finish()
        }


        // Sign with Email I'd and Password
        binding.signInBtn.setOnClickListener {
            val email = binding.emailEdt.text.toString()
            val password = binding.passwordEdt.text.toString()
            if (email.isEmpty()) binding.emailEdt.error = "Please enter email"
            if (password.isEmpty()) binding.passwordEdt.error = "Please enter password"
            else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Shared Preference
                        val pref = getSharedPreferences("login", MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putBoolean("flag", true)
                        editor.apply()
//                        Handler().postDelayed({
//                            val progressDialog = ProgressDialog(this@SignInActivity)
//                            progressDialog.setMessage("Application is loading, please wait")
//                            progressDialog.show()
//                        }, 2000)
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }
            binding.emailEdt.text = null
            binding.passwordEdt.text = null
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleSignUpBtn.setOnClickListener {
            signInGoogle()
        }

    }

    // Google Sign Option
    private fun signInGoogle() {
        val signInIntent = this.googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                // Shared Preference
                val pref = getSharedPreferences("login", MODE_PRIVATE)
                val editor = pref.edit()
                editor.putBoolean("flag", true)
                editor.apply()
//                Handler().postDelayed({
//                    val progressDialog = ProgressDialog(this@SignInActivity)
//                    progressDialog.setMessage("Application is loading, please wait")
//                    progressDialog.show()
//                }, 2000)

                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }
}