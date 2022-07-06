package com.economiaon.ui.navigation.financeschart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.data.model.Finance
import com.economiaon.databinding.FragmentOverviewFinancesBinding
import com.economiaon.ui.charts.FutureChartFragment
import com.economiaon.ui.charts.PresentChartFragment
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinancesOverviewFragment : Fragment() {

    private var _binding: FragmentOverviewFinancesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FinancesOverviewViewModel>()
    private val _userPrefs by lazy { UserPreferences(requireContext()) }
    private lateinit var financesList: ArrayList<Finance>
    private val dialog by lazy { requireContext().createProgressDialog() }
    private val periodTypes by lazy { resources.getStringArray(R.array.overview_types) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        lifecycleScope.launch {
            _userPrefs.userId.collect {
                if (it != null) {
                    viewModel.getFinancesByUserId(it)
                }
            }
        }

        _binding = FragmentOverviewFinancesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupDropDown()
        setupChart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDropDown() {
        val periodTypesArray = resources.getStringArray(R.array.overview_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, periodTypesArray)
        binding.actvPeriod.setAdapter(arrayAdapter)
    }

    private fun setupChart() {
        binding.actvPeriod.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent?.getItemAtPosition(position).toString()
                viewModel.financesList.observe(this) {
                    when (it) {
                        FinancesOverviewViewModel.State.Loading -> dialog.show()
                        is FinancesOverviewViewModel.State.Error -> {
                            requireContext().createDialog {
                                setMessage(it.error.message)
                            }
                        }
                        is FinancesOverviewViewModel.State.Success -> {
                            dialog.dismiss()
                            if (selectedItem == periodTypes[0]) {
                                childFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.chart_container, PresentChartFragment.newInstance(it.list as ArrayList<Finance>))
                                    .commit()
                            } else if (selectedItem == periodTypes[periodTypes.size - 1]) {
                                childFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.chart_container, FutureChartFragment.newInstance(it.list as ArrayList<Finance>))
                                    .commit()
                            }
                        }
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        viewModel.financesList.observe(viewLifecycleOwner) {
            when (it) {
                FinancesOverviewViewModel.State.Loading -> dialog.show()
                is FinancesOverviewViewModel.State.Error -> {
                    requireContext().createDialog {
                        setMessage(it.error.message)
                    }
                }
                is FinancesOverviewViewModel.State.Success -> {
                    dialog.dismiss()
                    financesList = it.list as ArrayList<Finance>
                }
            }
        }
    }
}