package com.example.sportapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var loginTextView: TextView
    private lateinit var skipTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Inisialisasi Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)

        // Menghubungkan view dengan ID
        usernameEditText = findViewById(R.id.Username)
        emailEditText = findViewById(R.id.Email)
        passwordEditText = findViewById(R.id.Password)
        confirmPasswordEditText = findViewById(R.id.ConfirmPassword)
        signupButton = findViewById(R.id.Register)
        loginTextView = findViewById(R.id.Login)
        skipTextView = findViewById(R.id.SkipNow)

        // Menangani klik tombol daftar
        signupButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                signUpUser(username, email, password)
            } else {
                showCustomToast("Harap masukkan data yang benar")
            }
        }

        // Menangani klik tombol login
        loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation)
        }

        // Menangani klik tombol skip
        skipTextView.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    // Fungsi untuk mendaftar pengguna
    private fun signUpUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Simpan username di SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("username", username)
                    editor.apply()

                    // Jika berhasil, tampilkan toast dan pindah ke LoginActivity
                    showCustomToast("Sign Up Berhasil")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation)
                    finish()
                } else {
                    // Jika gagal, tampilkan toast dengan pesan kesalahan
                    showCustomToast("Sign Up Gagal: ${task.exception?.message}")
                }
            }
    }

    // Fungsi untuk menampilkan custom toast
    private fun showCustomToast(message: String) {
        val toastView = layoutInflater.inflate(R.layout.custom_toast, null)
        val toastMessage = toastView.findViewById<TextView>(R.id.toast_message)
        toastMessage.text = message

        val toast = Toast(this)
        toast.view = toastView
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }
}
