package com.economiaon.ui.navigation.finances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.economiaon.R
import com.economiaon.databinding.FragmentOverviewFinancesBinding

class FinancesOverviewFragment : Fragment() {

    private var _binding: FragmentOverviewFinancesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDropDown() {
        val periodTypes = resources.getStringArray(R.array.overview_types)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, periodTypes)
        binding.actvPeriod.setAdapter(arrayAdapter)
    }
}