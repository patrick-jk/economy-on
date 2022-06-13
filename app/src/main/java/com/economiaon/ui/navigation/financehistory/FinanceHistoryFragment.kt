package com.economiaon.ui.navigation.financehistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.economiaon.databinding.FragmentLatestFinancesBinding
import com.economiaon.ui.FinanceListAdapter
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinanceHistoryFragment : Fragment() {

    private val dialog by lazy { context?.createProgressDialog() }
    private val viewModel by viewModel<FinanceHistoryViewModel>()
    private val adapter by lazy { FinanceListAdapter() }
    private var _binding: FragmentLatestFinancesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //TODO Fix getFinanceList function

        _binding = FragmentLatestFinancesBinding.inflate(inflater, container, false)
        binding.rvFinances.adapter = adapter

        viewModel.finances.observe(viewLifecycleOwner) {
            when (it) {
                FinanceHistoryViewModel.State.Loading -> dialog?.show()
                is FinanceHistoryViewModel.State.Error -> {
                    context?.createDialog {
                        setMessage(it.error.message)
                    }
                }
                is FinanceHistoryViewModel.State.Success -> {
                    dialog?.dismiss()
                    adapter.submitList(it.list)
                }
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}