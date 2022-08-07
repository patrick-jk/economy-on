package com.economiaon.presentation.edituser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.economiaon.R
import com.economiaon.databinding.ActivityEditUserBinding
import com.economiaon.domain.model.User
import com.economiaon.presentation.MainActivity
import com.economiaon.util.text
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditUserActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditUserBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<EditUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() {
        if (intent.hasExtra(USER_INFO)) {
            intent.extras?.let {
                val user = it.getParcelable<User>(USER_INFO) as User
                binding.apply {
                    tilUsername.text = user.name
                    tilPhone.text = user.cellphoneNumber
                    tilAge.text = user.age.toString()
                    tilSalary.text = user.salary.toString()

                    btnSaveChanges.setOnClickListener {
                        if (tilUsername.text.isBlank()) {
                            tilUsername.error = resources.getString(R.string.txt_invalid_username)
                            tilUsername.requestFocus()
                            return@setOnClickListener
                        }
                        if (tilPhone.text.isBlank()) {
                            tilPhone.error = resources.getString(R.string.txt_invalid_cellphone)
                            tilPhone.requestFocus()
                            return@setOnClickListener
                        }
                        if (tilAge.text.isBlank()) {
                            tilAge.error = resources.getString(R.string.txt_invalid_age)
                            tilAge.requestFocus()
                            return@setOnClickListener
                        }
                        if (tilSalary.text.isBlank()) {
                            tilSalary.error = resources.getString(R.string.txt_invalid_salary)
                            tilSalary.requestFocus()
                            return@setOnClickListener
                        }
                        viewModel.updateUser(User(user.id, tilUsername.text, user.cpf, user.email,
                            edtPhone.masked, user.password, tilAge.text.toInt(), tilSalary.text.toFloat()))
                    }
                }
            }
        }
    }

    companion object Extras {
        const val USER_INFO = "user_info"
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateUser.observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, R.string.txt_unexpected_error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}