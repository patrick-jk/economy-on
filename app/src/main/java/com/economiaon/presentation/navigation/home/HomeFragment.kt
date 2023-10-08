package com.economiaon.presentation.navigation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.economiaon.R
import com.economiaon.data.datastore.UserPreferences
import com.economiaon.databinding.FragmentHomeBinding
import com.economiaon.domain.model.Finance
import com.economiaon.presentation.statepattern.FinanceState
import com.economiaon.presentation.statepattern.State
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val dialog by lazy { requireContext().createProgressDialog() }
    private val _userPrefs by lazy { UserPreferences(requireContext()) }
    private val viewModel by viewModel<HomeViewModel>()
    private lateinit var finances: List<Finance>
    private var username = ""
    private var userSalary = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            _userPrefs.userId.collect { userId ->
                userId?.let {
                    async { viewModel.getUserById(it) }.await()
                    async {  viewModel.getFinanceList(it) }.await()
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

    @SuppressLint("SetTextI18n")
    private fun setupUi() {
        val financesSum = finances.map(Finance::financeCost).sumOf { it }

        binding.apply {
            tvUsername.text = resources.getString(R.string.txt_hello_user, username)
            if (userSalary > 0.0) {
                val spentSalaryPercentage = (financesSum * 100.0 / userSalary)
                tvUsedBalancePercent.text = resources.getString(R.string.txt_used_balance, spentSalaryPercentage) + "%"
                tvRemainingBalance.text = resources.getString(
                    R.string.txt_remaining_balance, NumberFormat.getCurrencyInstance(Locale.getDefault()).format((userSalary - financesSum))
                )

                if (financesSum > userSalary) {
                    tvBalanceStatus.text = resources.getString(R.string.txt_user_in_debit)
                    ivBalanceStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_sad_24dp, null))
                } else if (financesSum in (userSalary * 0.8)..userSalary) {
                    tvBalanceStatus.text = resources.getString(R.string.txt_finances_ok)
                    ivBalanceStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_neutral_24dp, null))
                } else {
                    tvBalanceStatus.text = resources.getString(R.string.txt_finance_savings)
                    ivBalanceStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_happy_24dp, null))
                }
            }

            mcvFinancesList.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_to_navigation_finances_list)
            }

            mcvChartsList.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_to_navigation_finances_chart)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> requireContext().createProgressDialog(it.loadingMessage)
                is State.Error -> {
                    requireContext().createDialog {
                        setMessage(it.error.message)
                    }.show()
                }

                is State.Success -> {
                    username = it.info.name
                    userSalary = it.info.salary.toDouble()
                    requireContext().createProgressDialog().dismiss()
                }
            }
            viewModel.finances.observe(viewLifecycleOwner) { financeState ->
                when (financeState) {
                    FinanceState.Loading -> dialog.show()
                    is FinanceState.Error -> {
                        context?.createDialog {
                            setMessage(financeState.error.message)
                        }
                    }
                    is FinanceState.Success -> {
                        finances = financeState.list
                        setupUi()
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}