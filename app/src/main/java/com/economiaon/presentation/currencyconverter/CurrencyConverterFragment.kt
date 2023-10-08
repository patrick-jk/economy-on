package com.economiaon.presentation.currencyconverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.economiaon.R
import com.economiaon.data.model.CurrencyConversionInfo
import com.economiaon.databinding.FragmentCurrencyConverterBinding
import com.economiaon.presentation.statepattern.State
import com.economiaon.util.Validator
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import com.economiaon.util.hideSoftKeyboard
import com.economiaon.util.text
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyConverterFragment : Fragment() {

    private var _binding: FragmentCurrencyConverterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<CurrencyConverterViewModel>()

    private val dialog by lazy { requireContext().createProgressDialog() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.apply {
            btnCalculateTax.setOnClickListener {
                with(Validator) {
                    val isAllFieldsFilled = validateField(tilCurrencyFrom, R.string.txt_invalid_currency, requireContext()) && validateField(
                        tilCurrencyTo,
                        R.string.txt_invalid_currency,
                        requireContext()
                    ) && validateField(tilValueToConvert, R.string.txt_invalid_value, requireContext())

                    if (isAllFieldsFilled) return@setOnClickListener

                    viewModel.convertCurrency(
                        CurrencyConversionInfo(
                            actvCurrencyFrom.text.toString().substringAfter(" - "),
                            actvCurrencyTo.text.toString().substringAfter(" - "),
                            tilValueToConvert.text.toDouble()
                        )
                    )
                }
            }

            actvCurrencyFrom.setOnClickListener {
                it.hideSoftKeyboard()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupDropDown()
    }

    private fun setupDropDown() {
        val periodTypesArray = resources.getStringArray(R.array.currency_names)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, periodTypesArray)
        binding.actvCurrencyFrom.setAdapter(arrayAdapter)
        binding.actvCurrencyTo.setAdapter(arrayAdapter)
    }

    override fun onStart() {
        super.onStart()
        viewModel.currencyConverted.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    dialog.show()
                }

                is State.Error -> {
                    dialog.dismiss()
                    requireContext().createDialog {
                        setMessage(it.error.message)
                    }.show()
                }

                is State.Success -> {
                    dialog.dismiss()
                    binding.tvResult.text = resources.getString(
                        R.string.txt_converted_value, String.format("%.3f", it.info.newAmount / it.info.oldAmount),
                        String.format("%.2f", it.info.newAmount)
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
