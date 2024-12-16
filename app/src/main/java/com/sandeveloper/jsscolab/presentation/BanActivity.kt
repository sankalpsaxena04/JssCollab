package com.sandeveloper.jsscolab.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivityBanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Linkify.addLinks(binding.email, Linkify.EMAIL_ADDRESSES)

        // Set up a click listener for the TextView
        binding.email.setOnClickListener {
            val email = binding.email.text.toString()
            composeEmail(arrayOf(email))
        }
        binding.btnClose.setOnClickListener {
            finish()
        }
        disableBackPress()

    }
    private fun refresh(){
        val intent = Intent(this, StartScreen::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun disableBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            refresh()
        }
    }

    private fun composeEmail(addresses: Array<String>) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}