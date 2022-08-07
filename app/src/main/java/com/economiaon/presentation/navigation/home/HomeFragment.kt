package com.economiaon.presentation.navigation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.databinding.FragmentHomeBinding
import com.economiaon.domain.model.Finance
import com.economiaon.presentation.addfinance.AddFinanceActivity
import com.economiaon.presentation.statepattern.FinanceState
import com.economiaon.presentation.statepattern.UserState
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val dialog by lazy { requireContext().createProgressDialog() }
    private val _userPrefs by lazy { UserPreferences(requireContext()) }
    private val viewModel by viewModel<HomeViewModel>()
    private lateinit var finances: List<Finance>
    private var userSalary: BigDecimal = 0.toBigDecimal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            _userPrefs.userId.collect { userId ->
                userId?.let {
                    viewModel.getFinanceList(it)
                    viewModel.getUserById(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupUi() {
        val financesSum = finances.map(Finance::financePrice).sumOf { it }

        binding.apply {
            if (userSalary > 0.toBigDecimal()) {
                tvUsedBalancePercent.text = "${
                    resources.getString(
                        R.string.txt_used_balance,
                        ((financesSum * 100.toBigDecimal()) / userSalary).toDouble()
                    )
                }%"
                tvRemainingBalance.text = resources.getString(
                    R.string.txt_remaining_balance,
                    (userSalary - financesSum).toDouble()
                )
                tvBalanceStatus.text = if (financesSum > userSalary) {
                    resources.getString(R.string.txt_user_in_dedit)
                } else if (financesSum in (userSalary * 0.8.toBigDecimal())..userSalary) {
                    resources.getString(R.string.txt_finances_ok)
                } else {
                    resources.getString(R.string.txt_finance_savings)
                }
            }
            tvAddFinance.setOnClickListener {
                startActivity(Intent(requireContext(), AddFinanceActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is UserState.Loading -> {}
                is UserState.Error -> {
                    context?.createDialog {
                        setMessage(it.error.message)
                    }
                }
                is UserState.Success -> {
                    userSalary = it.user.salary.toBigDecimal()
                }
            }
        }
        viewModel.finances.observe(viewLifecycleOwner) {
            when (it) {
                FinanceState.Loading -> dialog.show()
                is FinanceState.Error -> {
                    context?.createDialog {
                        setMessage(it.error.message)
                    }
                }
                is FinanceState.Success -> {
                    dialog.dismiss()
                    finances = it.list
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