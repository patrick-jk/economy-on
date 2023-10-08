package com.economiaon.presentation.taxcalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.economiaon.databinding.FragmentTaxCalculatorBinding
import com.economiaon.util.hideSoftKeyboard
import com.economiaon.util.text
import kotlinx.coroutines.launch

class TaxCalculatorFragment : Fragment() {
    private var _binding: FragmentTaxCalculatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TaxCalculatorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaxCalculatorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.apply {
            btnCalculateTax.setOnClickListener {
                val value = tilTaxableIncome.text.toDouble()
                val tax = tilTaxablePercentage.text.toDouble()
                viewModel.calculateValueAfterTaxes(value, tax)
                it.hideSoftKeyboard()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            viewModel.valueAfterTaxes.collect {
                binding.tvResult.text = it.toString()
            }
        }
    }
}