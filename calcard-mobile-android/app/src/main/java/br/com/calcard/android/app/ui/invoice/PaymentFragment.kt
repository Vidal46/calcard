package br.com.calcard.android.app.ui.invoice

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.InvoiceSumary
import br.com.calcard.android.app.ui.invoicepayment.InvoicePaymentViewModel
import br.com.calcard.android.app.utils.FormatterUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : BottomSheetDialogFragment() {

    val vModel: InvoicePaymentViewModel = InvoicePaymentViewModel()
    lateinit var sumary: InvoiceSumary

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sumary = companionSumary.value!!
        vModel.getBill(FormatterUtil.formatFullDateT(sumary.due.dueDate), sumary.balance.toString())
        payment_fragment_valor_total.text = FormatterUtil.formatCurrency(sumary.balance)
        setClickListener()
        setObeservers()

    }

    fun setClickListener() {
        payment_fragment_btn.setOnClickListener {}

        payment_fragment_share.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, vModel.bill.digitableLine.replace(".", "").replace(" ", ""))
            intent.type = "text/plain"
            startActivity(intent)
        }

        payment_fragment_btn.setOnClickListener {
            val clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("simple text", vModel.bill.digitableLine.replace(".", "").replace(" ", ""))
            clipboard.primaryClip = clip
            Toast.makeText(activity, "código copiado para a área de transferência", Toast.LENGTH_SHORT).show()
        }


    }

    companion object {
        lateinit var mutableFlag: MutableLiveData<Boolean>
        lateinit var companionSumary: MutableLiveData<InvoiceSumary>
    }

    fun setObeservers() {
        vModel.mutableLoadingState.observe(this, Observer {
            it?.let { bill ->
                payment_fragment_codigo.text = vModel.bill.digitableLine
            }
        })

    }
}
