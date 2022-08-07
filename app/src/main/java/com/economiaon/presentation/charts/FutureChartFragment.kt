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
        val socialEntry: ArrayList<BarEntry> = arrayListOf(
            BarEntry(1F, setupSocialFirstMonth()),
            BarEntry(2F, setupSocialSecondMonth()),
            BarEntry(3F, setupSocialThirdMonth()),
            BarEntry(4F, setupSocialForthMonth()),
            BarEntry(5F, setupSocialFifthMonth()),
            BarEntry(6F, setupSocialSixthMonth()))
        val securityEntry: ArrayList<BarEntry> = arrayListOf(
            BarEntry(1F, setupSecurityFirstMonth()),
            BarEntry(2F, setupSecuritySecondMonth()),
            BarEntry(3F, setupSecurityThirdMonth()),
            BarEntry(4F, setupSecurityForthMonth()),
            BarEntry(5F, setupSecurityFifthMonth()),
            BarEntry(6F, setupSecuritySixthMonth()))
        val physiologyEntry: ArrayList<BarEntry> = arrayListOf(
            BarEntry(1F, setupPhysiologyFirstMonth()),
            BarEntry(2F, setupPhysiologySecondMonth()),
            BarEntry(3F, setupPhysiologyThirdMonth()),
            BarEntry(4F, setupPhysiologyForthMonth()),
            BarEntry(5F, setupPhysiologyFifthMonth()),
            BarEntry(6F, setupPhysiologySixthMonth()))
        val leisureEntry: ArrayList<BarEntry> = arrayListOf(
            BarEntry(1F, setupLeisureFirstMonth()),
            BarEntry(2F, setupLeisureSecondMonth()),
            BarEntry(3F, setupLeisureThirdMonth()),
            BarEntry(4F, setupLeisureForthMonth()),
            BarEntry(5F, setupLeisureFifthMonth()),
            BarEntry(6F, setupLeisureSixthMonth()))
        val personalDevEntry: ArrayList<BarEntry> = arrayListOf(
            BarEntry(1F, setupPersonalDevFirstMonth()),
            BarEntry(2F, setupPersonalDevSecondMonth()),
            BarEntry(3F, setupPersonalDevThirdMonth()),
            BarEntry(4F, setupPersonalDevForthMonth()),
            BarEntry(5F, setupPersonalDevFifthMonth()),
            BarEntry(6F, setupPersonalDevSixthMonth()))

        val socialDataSet = BarDataSet(socialEntry, "Social Finances")
        socialDataSet.color = Color.RED
        val securityDataSet = BarDataSet(securityEntry, "Security Finances")
        securityDataSet.color = Color.BLUE
        val physiologyDataSet = BarDataSet(physiologyEntry, "Physiology Finances")
        physiologyDataSet.color = Color.GREEN
        val leisureDataSet = BarDataSet(leisureEntry, "Leisure Finances")
        leisureDataSet.color = Color.LTGRAY
        val personalDevDataSet = BarDataSet(personalDevEntry, "Personal Dev Finances")
        personalDevDataSet.color = Color.CYAN

        val barData = BarData(socialDataSet, securityDataSet, physiologyDataSet, leisureDataSet,
        personalDevDataSet)
        val types = arrayOf(
            context?.getString(R.string.first_month),
            context?.getString(R.string.second_month),
            context?.getString(R.string.third_month),
            context?.getString(R.string.forth_month),
            context?.getString(R.string.fifth_month),
            context?.getString(R.string.sixth_month))

        binding.apply {
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

    private fun setupSocialFirstMonth(): Float {
        return firstMonthData.filter {
            it.type == FinanceType.SOCIAL
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSecurityFirstMonth(): Float {
        return firstMonthData.filter {
            it.type == FinanceType.SECURITY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPhysiologyFirstMonth(): Float {
        return firstMonthData.filter {
            it.type == FinanceType.PHYSIOLOGY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupLeisureFirstMonth(): Float {
        return firstMonthData.filter {
            it.type == FinanceType.LEISURE
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPersonalDevFirstMonth(): Float {
        return firstMonthData.filter {
            it.type == FinanceType.PERSONAL_DEVELOPMENT
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSocialSecondMonth(): Float {
        return secondMonthData.filter {
            it.type == FinanceType.SOCIAL
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSecuritySecondMonth(): Float {
        return secondMonthData.filter {
            it.type == FinanceType.SECURITY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPhysiologySecondMonth(): Float {
        return secondMonthData.filter {
            it.type == FinanceType.PHYSIOLOGY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupLeisureSecondMonth(): Float {
        return secondMonthData.filter {
            it.type == FinanceType.LEISURE
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPersonalDevSecondMonth(): Float {
        return secondMonthData.filter {
            it.type == FinanceType.PERSONAL_DEVELOPMENT
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSocialThirdMonth(): Float {
        return thirdMonthData.filter {
            it.type == FinanceType.SOCIAL
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSecurityThirdMonth(): Float {
        return thirdMonthData.filter {
            it.type == FinanceType.SECURITY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPhysiologyThirdMonth(): Float {
        return thirdMonthData.filter {
            it.type == FinanceType.PHYSIOLOGY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupLeisureThirdMonth(): Float {
        return thirdMonthData.filter {
            it.type == FinanceType.LEISURE
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPersonalDevThirdMonth(): Float {
        return thirdMonthData.filter {
            it.type == FinanceType.PERSONAL_DEVELOPMENT
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSocialForthMonth(): Float {
        return forthMonthData.filter {
            it.type == FinanceType.SOCIAL
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSecurityForthMonth(): Float {
        return forthMonthData.filter {
            it.type == FinanceType.SECURITY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPhysiologyForthMonth(): Float {
        return forthMonthData.filter {
            it.type == FinanceType.PHYSIOLOGY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupLeisureForthMonth(): Float {
        return forthMonthData.filter {
            it.type == FinanceType.LEISURE
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPersonalDevForthMonth(): Float {
        return forthMonthData.filter {
            it.type == FinanceType.PERSONAL_DEVELOPMENT
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSocialFifthMonth(): Float {
        return fifthMonthData.filter {
            it.type == FinanceType.SOCIAL
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSecurityFifthMonth(): Float {
        return fifthMonthData.filter {
            it.type == FinanceType.SECURITY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPhysiologyFifthMonth(): Float {
        return fifthMonthData.filter {
            it.type == FinanceType.PHYSIOLOGY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupLeisureFifthMonth(): Float {
        return fifthMonthData.filter {
            it.type == FinanceType.LEISURE
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPersonalDevFifthMonth(): Float {
        return fifthMonthData.filter {
            it.type == FinanceType.PERSONAL_DEVELOPMENT
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSocialSixthMonth(): Float {
        return sixthMonthData.filter {
            it.type == FinanceType.SOCIAL
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupSecuritySixthMonth(): Float {
        return sixthMonthData.filter {
            it.type == FinanceType.SECURITY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPhysiologySixthMonth(): Float {
        return sixthMonthData.filter {
            it.type == FinanceType.PHYSIOLOGY
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupLeisureSixthMonth(): Float {
        return sixthMonthData.filter {
            it.type == FinanceType.LEISURE
        }.sumOf { it.financePrice }.toFloat()
    }

    private fun setupPersonalDevSixthMonth(): Float {
        return sixthMonthData.filter {
            it.type == FinanceType.PERSONAL_DEVELOPMENT
        }.sumOf { it.financePrice }.toFloat()
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
        for (i in 0 until financesList.size - 1) {
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