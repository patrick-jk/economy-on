package com.economiaon.presentation.navigation.generaltab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.economiaon.R
import com.economiaon.databinding.FragmentGeneralTabBinding

class GeneralTabFragment : Fragment() {

    private var _binding: FragmentGeneralTabBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.apply {
            btnCalculateTaxes.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_general_tab_to_navigation_tax_calculator)
            }
            btnCurrencyConverter.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_general_tab_to_navigation_currency_converter)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}