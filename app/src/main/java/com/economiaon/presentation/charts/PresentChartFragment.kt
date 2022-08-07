package com.economiaon.presentation.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.economiaon.R
import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.FinanceType
import com.economiaon.databinding.FragmentPresentChartBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PresentChartFragment : Fragment() {
    private var _binding: FragmentPresentChartBinding? = null
    private val binding get() = _binding!!
    private val chartColors = listOf(Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN)
    private val financesList: java.util.ArrayList<Finance> by lazy {
        arguments?.getParcelableArrayList<Finance>(FINANCES)
                as java.util.ArrayList<Finance>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPresentChartBinding.inflate(inflater, container, false)

        setupChart()
        return binding.root
    }

    private fun setupChart() {
        val pieDataSet = PieDataSet(dataInfo(), "")
        pieDataSet.colors = chartColors
        val pieData = PieData(pieDataSet)
        binding.apply {
            pieChartPresent.setUsePercentValues(true)
            pieChartPresent.data = pieData
            pieChartPresent.invalidate()
        }
    }

    companion object {
        const val FINANCES = "finances"
        fun newInstance(financeList: java.util.ArrayList<Finance>): PresentChartFragment {
            return PresentChartFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(FINANCES, financeList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dataInfo(): ArrayList<PieEntry> {
        val bySocialType = financesList.filter {
            it.type == FinanceType.SOCIAL
        }.sumOf {
            it.financePrice
        }
        val bySecurityType = financesList.filter {
            it.type == FinanceType.SECURITY
        }.sumOf { it.financePrice }
        val byLeisureType = financesList.filter {
            it.type == FinanceType.LEISURE
        }.sumOf { it.financePrice }
        val byPersonalDevelopmentType = financesList.filter {
            it.type == FinanceType.PERSONAL_DEVELOPMENT
        }.sumOf { it.financePrice }
        val byPhysiologyType = financesList.filter {
            it.type == FinanceType.PHYSIOLOGY
        }.sumOf { it.financePrice }
        if (bySocialType != 0.toBigDecimal() && bySecurityType != 0.toBigDecimal() &&
            byLeisureType != 0.toBigDecimal() && byPersonalDevelopmentType != 0.toBigDecimal() &&
                byPhysiologyType != 0.toBigDecimal()) {
            return arrayListOf(
                PieEntry(bySocialType.toFloat(),
                    requireContext().getString(R.string.finance_type_social)),
                PieEntry(bySecurityType.toFloat(),
                    requireContext().getString(R.string.finance_type_security)),
                PieEntry(byPhysiologyType.toFloat(),
                    requireContext().getString(R.string.finance_type_physiology)),
                PieEntry(byLeisureType.toFloat(),
                    requireContext().getString(R.string.finance_type_leisure)),
                PieEntry(byPersonalDevelopmentType.toFloat(),
                    requireContext().getString(R.string.finance_type_personal_development)))
        }
        return arrayListOf(PieEntry(bySocialType.toFloat(),
            ""),
            PieEntry(bySecurityType.toFloat(),
                ""),
            PieEntry(byPhysiologyType.toFloat(),
                ""),
            PieEntry(byLeisureType.toFloat(),
                ""),
            PieEntry(byPersonalDevelopmentType.toFloat(),
                ""))
    }

}