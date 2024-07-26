package com.example.sportapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val typingTextView1 = findViewById<TypingTextView>(R.id.typingTextView1)
        val typingTextView2 = findViewById<TypingTextView>(R.id.typingTextView2)

        typingTextView1.setTypingAnimation("Super Sport\nCommunity App", 4000L) {
            typingTextView2.setTypingAnimation("Find sparring\nopponents with\njust the tap of\na finger.", 4000L) {
                coroutineScope.launch {
                    delay(1000) // 1000 milliseconds (1 second) delay
                    navigateToNextScreen()
                }
            }
        }
    }

    private fun navigateToNextScreen() {
        // Check if user is already logged in or not
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userEmail = sharedPref.getString("USER_EMAIL", null)

        val intent = if (userEmail != null) {
            // If user email is found, navigate to MainActivity
            Intent(this, MainActivity::class.java)
        } else {
            // Otherwise, navigate to LoginActivity
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
