package com.economiaon.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.databinding.ActivityLoginBinding
import com.economiaon.util.Validator
import com.economiaon.util.text
import com.economiaon.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val _binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val _userPrefs by lazy { UserPreferences(this) }
    private val viewModel by viewModel<LoginViewModel>()

    companion object Extras {
        const val EMAIL = "USER_EMAIL"
        const val PASSWORD = "USER_PASSWORD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        val userLogged = runBlocking { _userPrefs.isUserLogged.first() }
        if (userLogged == true) {
            startMainActivity()
        }
        setupUi()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun setupUi() {
        if (intent.hasExtra(EMAIL) && intent.hasExtra(PASSWORD)) {
            intent?.extras.let {
                val email = it?.getString(EMAIL)
                val password = it?.getString(PASSWORD)
                _binding.apply {
                    tilEmail.text = email ?: ""
                    tilPassword.text = password ?: ""
                }
            }
        }
        _binding.apply {
            btnLogin.setOnClickListener {
                val context = root.context
                with(Validator) {
                    val validateEmail = validateEmailInfo(
                        tilEmail,
                        R.string.txt_invalid_email, context)
                    val validatePassword = validateField(
                        tilPassword,
                        R.string.txt_invalid_password, context)
                    val validateFields = validateEmail || validatePassword
                    if (validateFields) return@setOnClickListener
                }
                viewModel.checkUserInfo(tilEmail.text, tilPassword.text)
            }
            tvNoAccount.setOnClickListener {
                startActivity(Intent(applicationContext, RegisterActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isUserLogged.observe(this) {
            if (it) {
                lifecycleScope.launch {
                    _userPrefs.saveIsUserLogged(it)
                }
                startMainActivity()
            } else {
                Toast.makeText(this, R.string.txt_user_not_exists, Toast.LENGTH_SHORT).show()
            }
        }
    }
}