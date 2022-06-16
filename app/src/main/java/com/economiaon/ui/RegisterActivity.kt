package com.economiaon.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.economiaon.R
import com.economiaon.databinding.ActivityRegisterBinding
import com.economiaon.domain.User
import com.economiaon.util.Validator
import com.economiaon.util.text
import com.economiaon.viewmodel.RegisterActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    //TODO Store User instance in a Data Store
    //TODO Finish login and Update profile button (using flow)

    private val _binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<RegisterActivityViewModel>()

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
                    validateFieldAndSetErrorWhenInvalid(tilUsername, R.string.txt_invalid_username, context)
                    validateFieldAndSetErrorWhenInvalid(tilCpf, R.string.txt_invalid_cpf, context)
                    validateFieldAndSetErrorWhenInvalid(tilCellphoneNumber, R.string.txt_invalid_cellphone, context)
                    if (!validateEmailAndConfirmation(tilEmail.text, tilConfirmEmail.text)) {
                        validateFieldAndSetErrorWhenInvalid(tilConfirmEmail, R.string.txt_emails_different, context)
                    } else {
                        validateFieldAndSetErrorWhenInvalid(tilConfirmEmail, R.string.txt_invalid_email, context)
                    }
                    validateFieldAndSetErrorWhenInvalid(tilEmail, R.string.txt_invalid_email, context)
                    if (!validatePasswordAndConfirmation(tilPassword.text, tilConfirmPassword.text)) {
                        validateFieldAndSetErrorWhenInvalid(tilConfirmPassword, R.string.txt_password_different, context)
                    } else {
                        validateFieldAndSetErrorWhenInvalid(tilConfirmPassword, R.string.txt_invalid_password, context)
                    }
                    validateFieldAndSetErrorWhenInvalid(tilPassword, R.string.txt_invalid_password, context)
                    validateFieldAndSetErrorWhenInvalid(tilConfirmPassword, R.string.txt_invalid_password, context)
                    validateFieldAndSetErrorWhenInvalid(tilAge, R.string.txt_invalid_age, context)
                    validateFieldAndSetErrorWhenInvalid(tilSalary, R.string.txt_invalid_salary, context)
                }
                //TODO Fix Validation (Register is reachable even with invalid fields)
                //TODO Fix age and salary validation
//                viewModel.registerUser(User(0, tilUsername.text, tilCpf.text, tilEmail.text,
//                tilCellphoneNumber.text, tilPassword.text, tilAge.text.toInt(), tilSalary.text.toFloat()))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.status.observe(this) {
            if (it) {
                Toast.makeText(this, R.string.txt_user_registered, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.txt_user_fail_to_register, Toast.LENGTH_SHORT).show()
            }
        }
    }
}