package com.economiaon.presentation.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.economiaon.R
import com.economiaon.databinding.FragmentPresentChartBinding
import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.FinanceType

class PresentChartFragment : Fragment() {
    private var _binding: FragmentPresentChartBinding? = null
    private val binding get() = _binding!!
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
        val pieChart = AnyChart.pie()

        pieChart.apply {
            data(dataInfo())

            title(getString(R.string.txt_category_spending_title))
            legend().title().enabled(true)
            labels().fontSize(14)
            legend().title()
                .text(getString(R.string.txt_categories_legend))
                .padding(0.0, 0.0, 10.0, 0.0)
        }

        binding.pieChartPresent.setChart(pieChart)
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

    private fun dataInfo(): ArrayList<DataEntry> {
        val bySocialType = filterListByTypeAndSumSpending(FinanceType.SOCIAL)
        val bySecurityType = filterListByTypeAndSumSpending(FinanceType.SECURITY)
        val byLeisureType = filterListByTypeAndSumSpending(FinanceType.LEISURE)
        val byPersonalDevelopmentType = filterListByTypeAndSumSpending(FinanceType.PERSONAL_DEVELOPMENT)
        val byPhysiologyType = filterListByTypeAndSumSpending(FinanceType.PHYSIOLOGY)

        return arrayListOf(
            ValueDataEntry(requireContext().getString(R.string.finance_type_social), bySocialType.toFloat()),
            ValueDataEntry(requireContext().getString(R.string.finance_type_security), bySecurityType.toFloat()),
            ValueDataEntry(requireContext().getString(R.string.finance_type_physiology), byPhysiologyType.toFloat()),
            ValueDataEntry(requireContext().getString(R.string.finance_type_leisure), byLeisureType.toFloat()),
            ValueDataEntry(requireContext().getString(R.string.finance_type_personal_development), byPersonalDevelopmentType.toFloat())
        )
    }

    private fun filterListByTypeAndSumSpending(type: FinanceType) = financesList.filter {
        it.type == type
    }.sumOf {
        it.financeCost
    }
}