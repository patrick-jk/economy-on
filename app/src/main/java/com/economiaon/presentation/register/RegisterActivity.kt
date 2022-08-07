package com.economiaon.presentation.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.databinding.ActivityRegisterBinding
import com.economiaon.domain.model.User
import com.economiaon.presentation.login.LoginActivity
import com.economiaon.presentation.statepattern.UserState
import com.economiaon.util.*
import kotlinx.coroutines.runBlocking
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
                        context)
                    val validateCpf = if (edtCpf.unMasked.isBlank()) {
                        tilCpf.error = resources.getString(R.string.txt_invalid_cpf)
                        tilCpf.requestFocus()
                        true
                    } else {
                        tilCpf.isErrorEnabled = false
                        false
                    }
                    val validateCellphone = if (edtPhone.unMasked.isBlank()) {
                        tilCellphoneNumber.error = resources.getString(R.string.txt_invalid_cellphone)
                        tilCellphoneNumber.requestFocus()
                        true
                    } else {
                        tilCellphoneNumber.isErrorEnabled = false
                        false
                    }
                    val validateEmail =
                        validateEmailInfo(tilEmail, R.string.txt_invalid_email, context)
                    val validatePassword = validateField(
                        tilPassword, R.string.txt_invalid_password,
                        context)
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
                if (viewModel.isUserAlreadyRegistered.value == true) return@setOnClickListener

                viewModel.saveUser(User(id = 0, name = tilUsername.text, cpf = edtCpf.masked, email = tilEmail.text,
                    cellphoneNumber = edtPhone.masked, password = tilPassword.text, age = tilAge.text.toInt(),
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
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(LoginActivity.EMAIL, _binding.tilEmail.text)
                intent.putExtra(LoginActivity.PASSWORD, _binding.tilPassword.text)
                startActivity(intent)
            } else {
                Toast.makeText(this, R.string.txt_user_fail_to_register, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isUserAlreadyRegistered.observe(this) {
            if (it) {
                Toast.makeText(this, R.string.txt_user_already_registered, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.registerUser.observe(this) {
            when (it) {
                is UserState.Loading -> createProgressDialog().show()
                is UserState.Error -> {
                    createDialog {
                        setMessage(it.error.message)
                    }
                }
                is UserState.Success -> {
                    runBlocking { _userPrefs.saveUserId(it.user.id) }
                    createProgressDialog().dismiss()
                }
            }
        }
    }
}