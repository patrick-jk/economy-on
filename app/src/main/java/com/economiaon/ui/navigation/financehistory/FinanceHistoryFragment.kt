package com.economiaon.ui.navigation.financehistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.economiaon.databinding.FragmentLatestFinancesBinding

class FinanceHistoryFragment : Fragment() {

    private var _binding: FragmentLatestFinancesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val financeHistoryViewModel =
            ViewModelProvider(this)[FinanceHistoryViewModel::class.java]

        _binding = FragmentLatestFinancesBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}