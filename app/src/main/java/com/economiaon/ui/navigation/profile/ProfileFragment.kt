package com.economiaon.ui.navigation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.data.model.User
import com.economiaon.databinding.FragmentProfileBinding
import com.economiaon.ui.EditUserActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private val viewModel by viewModel<ProfileViewModel>()
    private var _binding: FragmentProfileBinding? = null
    private val _userPrefs by lazy { UserPreferences(requireContext()) }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }

    private fun setupUi() {
        _binding?.apply {
            lifecycleScope.launch {
                _userPrefs.userId.collect {
                    if (it != null) {
                        viewModel.getUserById(it)
                    }
                }
            }
            viewModel.userInfo.observe(requireActivity()) {
                if (it != null) {
                    tvUserName.text = requireContext().getString(R.string.txt_user_info_name, it.name)
                    tvUserAge.text = requireContext().getString(R.string.txt_user_info_age, it.age)
                    tvUserMoney.text = requireContext().getString(R.string.txt_user_info_salary, it.salary)
                    tvUserPhone.text = requireContext().getString(R.string.txt_user_info_phone, it.cellphoneNumber)
                }
            }
            btnUpdateUser.setOnClickListener {
                val intent = Intent(requireContext(), EditUserActivity::class.java)
                intent.putExtra(EditUserActivity.USER_INFO, viewModel.userInfo.value as User)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}