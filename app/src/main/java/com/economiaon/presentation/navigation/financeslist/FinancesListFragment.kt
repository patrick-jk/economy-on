package com.economiaon.presentation.navigation.financeslist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.economiaon.data.UserPreferences
import com.economiaon.databinding.FragmentFinancesListBinding
import com.economiaon.presentation.FinanceListAdapter
import com.economiaon.presentation.addfinance.AddFinanceActivity
import com.economiaon.presentation.statepattern.FinanceState
import com.economiaon.util.createDialog
import com.economiaon.util.createProgressDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinancesListFragment : Fragment() {

    private val dialog by lazy { requireContext().createProgressDialog() }
    private val viewModel by viewModel<FinancesListViewModel>()
    private val adapter by lazy {
        FinanceListAdapter {
            val intent = Intent(context, AddFinanceActivity::class.java)
            intent.putExtra(AddFinanceActivity.FINANCE, it)
            context?.startActivity(intent)
        }
    }

    private var _binding: FragmentFinancesListBinding? = null
    private val binding get() = _binding!!
    private val _userPrefs by lazy { UserPreferences(requireContext()) }
    private var userId: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        lifecycleScope.launch {
            _userPrefs.userId.collect { userId ->
                userId?.let {
                    viewModel.getFinanceList(userId)
                    this@FinancesListFragment.userId = it
                }
            }
        }

        _binding = FragmentFinancesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFinances.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
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
                    adapter.submitList(it.list)
                    if (it.list.isNotEmpty()) binding.tvTitle.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}