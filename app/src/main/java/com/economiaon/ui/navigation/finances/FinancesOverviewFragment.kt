package com.economiaon.ui.navigation.finances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.economiaon.R
import com.economiaon.databinding.FragmentOverviewFinancesBinding
import com.economiaon.ui.FutureChartFragment
import com.economiaon.ui.PresentChartFragment

class FinancesOverviewFragment : Fragment() {

    private var _binding: FragmentOverviewFinancesBinding? = null
    private val binding get() = _binding!!

    private val periodTypes by lazy { resources.getStringArray(R.array.overview_types) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this)[FinancesOverviewViewModel::class.java]

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
                if (selectedItem == periodTypes[0]) {
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.chart_container, PresentChartFragment.newInstance())
                        .commit()
                } else if (selectedItem == periodTypes[periodTypes.size - 1]) {
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.chart_container, FutureChartFragment.newInstance())
                        .commit()
                }
            }
    }
}