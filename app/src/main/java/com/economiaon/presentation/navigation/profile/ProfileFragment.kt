package com.economiaon.presentation.navigation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.databinding.FragmentProfileBinding
import com.economiaon.domain.model.User
import com.economiaon.presentation.edituser.EditUserActivity
import com.economiaon.presentation.statepattern.UserState
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel by viewModel<ProfileViewModel>()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val _userPrefs by lazy { UserPreferences(requireContext()) }
    private val dialog by lazy { requireContext().createProgressDialog() }
    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceUserState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        runBlocking {
            _userPrefs.userId.collect {
                it?.let { id ->
                    viewModel.getUserById(id)
                }
            }
        }
        return binding.root
    }

    private fun setupUi() {
        binding.apply {
            tvUserName.text = context?.getString(R.string.txt_user_info_name, user.name)
            tvUserAge.text = context?.getString(R.string.txt_user_info_age, user.age)
            tvUserMoney.text = context?.getString(R.string.txt_user_info_salary, user.salary)
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
                UserState.Loading -> dialog.show()
                is UserState.Error -> {
                    context?.createDialog {
                        setMessage(it.error.message)
                    }
                }
                is UserState.Success -> {
                    dialog.dismiss()
                    user = it.user
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