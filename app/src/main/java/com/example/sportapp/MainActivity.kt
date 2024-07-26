package com.example.sportapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Handler
import android.os.Looper
import android.app.ActivityOptions

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var welcomeTextView: TextView
    private lateinit var detailButton: Button
    private val typingSpeed = 200L // Kecepatan mengetik dalam milidetik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)

        // Menghubungkan view dengan ID
        welcomeTextView = findViewById(R.id.welcomeTextView)
        detailButton = findViewById(R.id.Detail)

        // Ambil username dari SharedPreferences
        val username = sharedPreferences.getString("username", "User") ?: "User"
        val welcomeMessage = "Welcome, $username!"

        // Set animasi mengetik
        animateTyping(welcomeMessage)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> selectedFragment = HomeFragment()
                R.id.nav_search -> selectedFragment = ProfileFragment()
                R.id.nav_cart -> selectedFragment = ProfileFragment()
                R.id.nav_notif -> selectedFragment = NotifFragment()
                R.id.nav_profile -> selectedFragment = ProfileFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }
            true
        }

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Handle button click with fade in and fade out animations
        detailButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)
            startActivity(intent, options.toBundle())
        }
    }

    private fun animateTyping(message: String) {
        val handler = Handler(Looper.getMainLooper())
        val delay = typingSpeed
        var index = 0

        handler.post(object : Runnable {
            override fun run() {
                if (index < message.length) {
                    welcomeTextView.text = message.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, delay)
                }
            }
        })
    }
}

