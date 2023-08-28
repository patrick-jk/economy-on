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
import com.economiaon.databinding.FragmentFutureChartBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.LocalDate
import java.time.Month
import java.time.Period

class FutureChartFragment : Fragment() {
    private var _binding: FragmentFutureChartBinding? = null
    private val binding get() = _binding!!
    private val financesList: ArrayList<Finance> by lazy {
        arguments?.getParcelableArrayList<Finance>(FINANCES_FUTURE) as ArrayList<Finance>
    }

    private lateinit var firstMonthData: List<Finance>
    private lateinit var secondMonthData: List<Finance>
    private lateinit var thirdMonthData: List<Finance>
    private lateinit var forthMonthData: List<Finance>
    private lateinit var fifthMonthData: List<Finance>
    private lateinit var sixthMonthData: List<Finance>

    private val months = enumValues<Month>()

    private val financeTypeColors = mapOf(
        FinanceType.SOCIAL to Color.RED,
        FinanceType.SECURITY to Color.BLUE,
        FinanceType.PHYSIOLOGY to Color.GREEN,
        FinanceType.LEISURE to Color.LTGRAY,
        FinanceType.PERSONAL_DEVELOPMENT to Color.CYAN
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFutureChartBinding.inflate(inflater, container, false)
        separateDataByMonth()
        setupChart()
        return binding.root
    }

    private fun setupChart() {
        val entriesMap = mutableMapOf<FinanceType, ArrayList<BarEntry>>()

        FinanceType.values().forEach { category ->
            val entryList = ArrayList<BarEntry>()
            months.forEachIndexed { index, month ->
                val monthData = separateDataByMonth(month, category)
                entryList.add(BarEntry(index.toFloat(), sumOfFinancePrices(monthData)))
            }
            entriesMap[category] = entryList
        }

        val barDataSets = entriesMap.map { (category, entryList) ->
            BarDataSet(entryList, category.toString()).apply {
                color = financeTypeColors[category] ?: Color.BLACK
            }
        }

        val types = arrayOf(
            context?.getString(R.string.first_month),
            context?.getString(R.string.second_month),
            context?.getString(R.string.third_month),
            context?.getString(R.string.forth_month),
            context?.getString(R.string.fifth_month),
            context?.getString(R.string.sixth_month))

        binding.apply {
            val barData = BarData(*barDataSets.toTypedArray())
            barChartFuture.data = barData

            barChartFuture.data = barData
            val xAxis = barChartFuture.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(types)
            xAxis.setCenterAxisLabels(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            barChartFuture.isDragEnabled = true
            barChartFuture.setVisibleXRangeMaximum(3F)

            val barSpace = 0.04F
            val groupSpace = 0.32F
            barData.barWidth = 0.10F

            barChartFuture.xAxis.axisMinimum = 0F
            barChartFuture.xAxis.axisMaximum = 0 + barChartFuture.barData.getGroupWidth(groupSpace, barSpace) * 6
            barChartFuture.axisLeft.axisMinimum = 0F
            barChartFuture.groupBars(0F, groupSpace, barSpace)

            barChartFuture.invalidate()
        }
    }

    private fun separateDataByMonth(month: Month, type: FinanceType): List<Finance> {
        return financesList.filter {
            it.type == type &&
                    Period.between(LocalDate.parse(it.initialDate), LocalDate.parse(it.finalDate)).months == month.value
        }
    }

    private fun sumOfFinancePrices(data: List<Finance>): Float {
        return data.sumOf { it.financePrice }.toFloat()
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
    
    private fun separateDataByMonth() {
        for (i in 0 until financesList.size) {
            financesList[i].type = toFinanceTypeEnum(financesList[i].type.toString())
        }
        firstMonthData = financesList.filter {
            Period.between(LocalDate.parse(it.initialDate), LocalDate.parse(it.finalDate)).months <= 1
        }
        secondMonthData = financesList.filter {
            Period.between(LocalDate.parse(it.initialDate), LocalDate.parse(it.finalDate)).months == 2
        }
        thirdMonthData = financesList.filter {
            Period.between(LocalDate.parse(it.initialDate), LocalDate.parse(it.finalDate)).months == 3
        }
        forthMonthData = financesList.filter {
            Period.between(LocalDate.parse(it.initialDate), LocalDate.parse(it.finalDate)).months == 4
        }
        fifthMonthData = financesList.filter {
            Period.between(LocalDate.parse(it.initialDate), LocalDate.parse(it.finalDate)).months == 5
        }
        sixthMonthData = financesList.filter {
            Period.between(LocalDate.parse(it.initialDate), LocalDate.parse(it.finalDate)).months >= 6
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
}