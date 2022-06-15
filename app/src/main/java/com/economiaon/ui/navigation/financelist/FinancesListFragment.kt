package com.economiaon.ui.navigation.financelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.economiaon.databinding.FragmentFinancesListBinding
import com.economiaon.ui.FinanceListAdapter
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinancesListFragment : Fragment() {

    private val dialog by lazy { requireContext().createProgressDialog() }
    private val viewModel by viewModel<FinancesListViewModel>()
    private val adapter by lazy { FinanceListAdapter() }
    private var _binding: FragmentFinancesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //TODO Fix getFinanceList function

        _binding = FragmentFinancesListBinding.inflate(inflater, container, false)
        binding.rvFinances.adapter = adapter

        viewModel.finances.observe(viewLifecycleOwner) {
            when (it) {
                FinancesListViewModel.State.Loading -> dialog.show()
                is FinancesListViewModel.State.Error -> {
                    requireContext().createDialog {
                        setMessage(it.error.message)
                    }
                }
                is FinancesListViewModel.State.Success -> {
                    dialog.dismiss()
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