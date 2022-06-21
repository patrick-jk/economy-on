package com.economiaon.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.economiaon.R
import com.economiaon.data.model.User
import com.economiaon.databinding.ActivityEditUserBinding
import com.economiaon.ui.navigation.profile.ProfileFragment
import com.economiaon.util.text
import com.economiaon.viewmodel.EditUserViewModel
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
        intent.extras?.let {
            val user = it.getParcelable<User>(USER_INFO)
            binding.apply {
                if (user != null) {
                    tilUsername.text = user.name
                    tilPhone.text = user.cellphoneNumber
                    tilAge.text = user.age.toString()
                    tilSalary.text = user.salary.toString()
                }
                btnSaveChanges.setOnClickListener {
                    if (user != null) {
                        viewModel.updateUser(User(user.id, tilUsername.text, user.cpf,
                        user.email, tilPhone.text, user.password, tilAge.text.toInt(),
                        tilSalary.text.toFloat()))
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
                supportFragmentManager.beginTransaction()
                    .add(R.id.navigation_profile, ProfileFragment())
            }
            else {
                Toast.makeText(this, R.string.txt_unexpected_error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}