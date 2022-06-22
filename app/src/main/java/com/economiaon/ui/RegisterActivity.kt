package com.economiaon.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.data.model.User
import com.economiaon.databinding.ActivityRegisterBinding
import com.economiaon.util.Validator
import com.economiaon.util.hideSoftKeyboard
import com.economiaon.util.text
import com.economiaon.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    //TODO Finish login and Update profile button (using flow)
    private val _binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<RegisterViewModel>()
    private val _userPrefs by lazy { UserPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)

        setupUi()
    }

    private fun setupUi() {
        _binding.apply {
            btnRegister.setOnClickListener {
                val context = root.context
                with(Validator) {
                    val validateUsername = validateField(
                        tilUsername, R.string.txt_invalid_username,
                        context
                    )
                    val validateCpf = validateField(tilCpf, R.string.txt_invalid_cpf, context)
                    val validateCellphone = validateField(
                        tilCellphoneNumber,
                        R.string.txt_invalid_cellphone, context
                    )
                    val validateEmail =
                        validateEmailInfo(tilEmail, R.string.txt_invalid_email, context)
                    val validatePassword = validateField(
                        tilPassword, R.string.txt_invalid_password,
                        context
                    )
                    val validateEmailConfirmation = if (tilEmail.text != tilConfirmEmail.text) {
                        tilConfirmEmail.error = resources.getString(R.string.txt_emails_different)
                        tilConfirmEmail.requestFocus()
                        true
                    } else {
                        validateEmailInfo(tilConfirmEmail, R.string.txt_invalid_email, context)
                    }
                    val validatePasswordConfirmation =
                        if (tilPassword.text != tilConfirmPassword.text) {
                            tilConfirmPassword.error =
                                resources.getString(R.string.txt_password_different)
                            tilConfirmEmail.requestFocus()
                            true
                        } else {
                            validateField(
                                tilConfirmPassword, R.string.txt_invalid_password,
                                context
                            )
                        }
                    val validateAge = validateField(tilAge, R.string.txt_invalid_age, context)
                    val validateSalary = validateField(
                        tilSalary, R.string.txt_invalid_salary,
                        context
                    )
                    val validateFields = validateUsername || validateCpf || validateCellphone
                            || validateCellphone || validateEmailConfirmation || validatePasswordConfirmation
                            || validateEmail || validatePassword || validateAge || validateSalary
                    it.hideSoftKeyboard()
                    if (validateFields) return@setOnClickListener
                }
                viewModel.findUserByEmail(tilEmail.text)
                if (viewModel.isUserAlreadyRegistered.value == true) {
                    return@setOnClickListener
                }
                viewModel.registerUser(User(id = 0, name = tilUsername.text, cpf = tilCpf.text,
                        email = tilEmail.text, cellphoneNumber = tilCellphoneNumber.text,
                        password = tilPassword.text, age = tilAge.text.toInt(),
                        salary = tilSalary.text.toFloat()))
            }
            tvAlreadyRegistered.setOnClickListener {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.status.observe(this) {
            if (it) {
                Toast.makeText(this, R.string.txt_user_registered, Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra(LoginActivity.EMAIL, _binding.tilEmail.text)
                intent.putExtra(LoginActivity.PASSWORD, _binding.tilPassword.text)
            } else {
                Toast.makeText(this, R.string.txt_user_fail_to_register, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isUserAlreadyRegistered.observe(this) {
            if (it) {
                Toast.makeText(this, R.string.txt_user_already_registered, Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                Toast.makeText(this, R.string.txt_user_registered, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.registerUser.observe(this) {
            lifecycleScope.launch {
                _userPrefs.saveUserId(it.id)
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra(LoginActivity.EMAIL, it.email)
                intent.putExtra(LoginActivity.PASSWORD, it.password)
            }
        }
    }
}