package com.economiaon.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.economiaon.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val _binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }
}