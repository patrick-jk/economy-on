package com.economiaon.presentation.charts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.economiaon.R
import com.economiaon.databinding.FragmentFutureChartBinding
import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.FinanceType
import java.time.LocalDate
import java.time.Period

class FutureChartFragment : Fragment() {
    private var _binding: FragmentFutureChartBinding? = null
    private val binding get() = _binding!!

    private val financesList: ArrayList<Finance> by lazy {
        arguments?.getParcelableArrayList<Finance>(FINANCES_FUTURE) as ArrayList<Finance>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFutureChartBinding.inflate(inflater, container, false)
        castStringToEnum()
        setupChart()
        return binding.root
    }

    private fun castStringToEnum() {
        financesList.forEach {
            it.type = toFinanceTypeEnum(it.type.toString())
        }
    }

    private fun setupChart() {
        val cartesian = AnyChart.column()

        val data: List<DataEntry> = arrayListOf(
            ValueDataEntry("1", sumOfFinancePrices(financesList, 1)),
            ValueDataEntry("2", sumOfFinancePrices(financesList, 2)),
            ValueDataEntry("3", sumOfFinancePrices(financesList, 3)),
            ValueDataEntry("4", sumOfFinancePrices(financesList, 4)),
            ValueDataEntry("5", sumOfFinancePrices(financesList, 5)),
            ValueDataEntry("6", sumOfFinancePrices(financesList, 6))
        )


        val column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0)
            .offsetY(5)
            .format("\${%Value}{groupsSeparator: }")

        cartesian.apply {
            animation(true)
            title(getString(R.string.txt_future_spendings))

            yScale().minimum(0)

            yAxis(0).labels().format("\${%Value}{groupsSeparator: }");

            tooltip().positionMode(TooltipPositionMode.POINT);
            interactivity().hoverMode(HoverMode.BY_X);

            xAxis(0).title(getString(R.string.txt_months))
            yAxis(0).title(getString(R.string.txt_spendings))
        }


        binding.apply {
            barChartFuture.setChart(cartesian)
        }
    }

    private fun checkIfFinanceIsGreaterThanOneYear(finance: Finance, initialDate: LocalDate): Int {
        val periodMonths = Period.between(initialDate, LocalDate.parse(finance.finalDate)).months

        val finalDate = LocalDate.parse(finance.finalDate)

        return if (Period.between(initialDate, finalDate).years >= 1) {
            periodMonths + Period.between(initialDate, finalDate).years * 12
        } else {
            periodMonths
        }
    }

    private fun sumOfFinancePrices(data: List<Finance>, monthPos: Int): Double {
        return when (monthPos) {
            1 -> {
                data.filter { LocalDate.now().plusMonths(1) in LocalDate.parse(it.initialDate)..LocalDate.parse(it.finalDate) }
                    .sumOf { it.financeCost }
            }

            in 2..6 -> {
                data.filter {
                    checkIfFinanceIsGreaterThanOneYear(it, LocalDate.now()) >= monthPos && LocalDate.now()
                        .plusMonths(monthPos.toLong()) in LocalDate.parse(it.initialDate)..LocalDate.parse(it.finalDate)
                }
                    .sumOf { it.financeCost }
            }

            else -> {
                0.0
            }
        }
    }

    private fun toFinanceTypeEnum(string: String): FinanceType {
        return when (string) {
            "PHYSIOLOGY" -> FinanceType.PHYSIOLOGY
            "SECURITY" -> FinanceType.SECURITY
            "LEISURE" -> FinanceType.LEISURE
            "SOCIAL" -> FinanceType.SOCIAL
            "PERSONAL_DEVELOPMENT" -> FinanceType.PERSONAL_DEVELOPMENT
            else -> FinanceType.SOCIAL
        }
    }

    companion object {
        const val FINANCES_FUTURE = "finances_future"
        fun newInstance(financeList: ArrayList<Finance>): FutureChartFragment {
            return FutureChartFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(FINANCES_FUTURE, financeList)
                }
            }
        }
    }
}