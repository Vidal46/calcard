package br.com.calcard.android.app.ui.ouze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Spending
import br.com.calcard.android.app.utils.FormatterUtil
import kotlinx.android.synthetic.main.fragment_spending_detail_bottom_sheet.*

class SpendingDetailBottomSheetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_spending_detail_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListener()
        bindViews()
    }

    private fun setClickListener() {
        spending_bottomsheet_btn.setOnClickListener {
            mutableFlag.value = true
        }
    }

    private fun bindViews() {
        spending_bottomsheet_sub_title.text = mutableSpending.value?.establishment
        spending_bottomsheet_date.text = (FormatterUtil.formatFullDate(mutableSpending.value?.eventDate!!) + ", às " + mutableSpending.value?.eventDate!!.split('T')[1])
        spending_bottomsheet_value.text = if (mutableSpending.value?.value != null) FormatterUtil.formatCurrency(mutableSpending.value?.value) else ""
        spending_bottomsheet_image.setImageResource(getIcon(mutableSpending.value?.expenseGoal?.name.toString()))
        spending_bottomsheet_type.text = mutableSpending.value?.description
    }

    fun getIcon(tipo: String): Int {
        return when (tipo.toLowerCase()) {
            "mercado" -> R.drawable.ic_detalhe_mercado
            "restaurante" -> R.drawable.ic_detalhe_restaurante
            "lazer" -> R.drawable.ic_detalhe_lazer
            "saude" -> R.drawable.ic_detalhe_saude
            "transporte" -> R.drawable.ic_detalhe_transporte

            else -> {
                R.drawable.ic_detalhe_outros
            }
        }

    }

    companion object {
        @JvmStatic
        lateinit var mutableFlag: MutableLiveData<Boolean>

        @JvmStatic
        lateinit var mutableSpending: MutableLiveData<Spending>
    }

}
