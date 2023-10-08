package com.economiaon.presentation.navigation.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.datastore.UserPreferences
import com.economiaon.databinding.FragmentProfileBinding
import com.economiaon.domain.model.User
import com.economiaon.presentation.edituser.EditUserActivity
import com.economiaon.presentation.statepattern.State
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class ProfileFragment : Fragment() {

    private val viewModel by viewModel<ProfileViewModel>()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val _userPrefs by lazy { UserPreferences(requireContext()) }
    private val dialog by lazy { requireContext().createProgressDialog() }
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            _userPrefs.userId.collect {
                it?.let { id ->
                    viewModel.getUserById(id)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceUserState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupUi() {
        binding.apply {
            tvUserName.text = context?.getString(R.string.txt_user_info_name, user.name)
            tvUserAge.text = context?.getString(R.string.txt_user_info_age, user.age)
            tvUserMoney.text = context?.getString(R.string.txt_user_info_salary, String.format(Locale.getDefault(), "$%.2f", user.salary))
            tvUserPhone.text = context?.getString(R.string.txt_user_info_phone, user.cellphoneNumber)

            btnUpdateUser.setOnClickListener {
                val intent = Intent(context, EditUserActivity::class.java)
                intent.putExtra(EditUserActivity.USER_INFO, user)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.userInfo.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> dialog.show()
                is State.Error -> {
                    requireContext().createDialog {
                        setMessage(it.error.message)
                    }.show()
                }

                is State.Success -> {
                    dialog.dismiss()
                    user = it.info
                    setupUi()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}