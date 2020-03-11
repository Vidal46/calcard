package br.com.calcard.android.app.ui.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Transaction
import br.com.calcard.android.app.utils.FormatterUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_invoice_deatil.*


class InvoiceDetailFragment : BottomSheetDialogFragment() {

    lateinit var transaction: Transaction

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_invoice_deatil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObject()
        invoice_detail_value.text = FormatterUtil.formatCurrency(transaction.real)
        invoice_detail_date.text = (FormatterUtil.formatFullDate(transaction.transactionDateTime) + ", às " + transaction.transactionDateTime.split('T')[1])
        invoice_detail_local.text = transaction.establishmentName
        invoice_fragment_tipo.text = transaction.descriptionAbbreviated
        invoice_detail_img.setImageResource(getIcon(transaction.expenseGoalName))

        if (transaction.installment != null && transaction.installmentQuantity != null && transaction.installmentQuantity > 0) {
            invoice_fragment_parcela.visibility = View.VISIBLE
            invoice_fragment_parcela.text = ("Parcela " + transaction.installment + " de " + transaction.installmentQuantity)
        }
        setClickListener()
    }

    private fun bindObject() {
        transaction = companionTransaction.value!!
    }

    companion object {
        @JvmStatic
        var companionTransaction = MutableLiveData<Transaction>()
        lateinit var mutableFlag: MutableLiveData<Boolean>

    }

    private fun setClickListener() {
        invoice_detail_btn.setOnClickListener {
            mutableFlag.value = true
        }
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
}


