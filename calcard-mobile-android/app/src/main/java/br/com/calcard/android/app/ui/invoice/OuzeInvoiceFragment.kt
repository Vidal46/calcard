package br.com.calcard.android.app.ui.invoice

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.InvoiceSumary
import br.com.calcard.android.app.model.Transaction
import br.com.calcard.android.app.ui.ouze.BottomSheetActions
import br.com.calcard.android.app.ui.ouze.CustomBottomSheetDialogFragment
import br.com.calcard.android.app.ui.ouze.CustomBottomSheetDialogFragment.Companion.companionSumary
import br.com.calcard.android.app.ui.ouze.CustomBottomSheetDialogFragment.Companion.companionTransaction
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.FormatterUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView
import kotlinx.android.synthetic.main.fragment_ouze_invoice.*


class OuzeInvoiceFragment(val invoiceSumary: InvoiceSumary) : Fragment() {

    val vModel = InvoiceViewModel()
    lateinit var bottomSheet: BottomSheetDialogFragment
    val flag = MutableLiveData<Boolean>()
    val paymentFlag = MutableLiveData<Boolean>()
    lateinit var adapter: InvoiceMenuAdapter
    lateinit var touchables: ArrayList<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ouze_invoice, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        invoice_fragment_vencimento.text = "Vencimento em " + "${FormatterUtil.formatDate(invoiceSumary.due.dueDate)}"
        invoice_fragment_invoice_title.text = getType(invoiceSumary.status)
        invoice_fragment_valor_total.text = FormatterUtil.formatCurrency(invoiceSumary.balance)
        invoice_fragment_fechamento.text = "Compras até " + "${FormatterUtil.formatDate(invoiceSumary.endDate)}"
        invoice_fragment_fatura_vazia.visibility = View.GONE
        configureColor(invoiceSumary.status)

        if (invoiceSumary.status == "OPEN") {

        } else {
            if (invoiceSumary.paid) {
                invoice_fragment_baixar_fatura.visibility = View.VISIBLE
            } else if (invoiceSumary.availability == "AVAILABLE") {
                if (invoiceSumary.balance > 0) {
                    invoice_fragment_baixar.visibility = View.VISIBLE
                    invoice_fragment_pagar_fatura.visibility = View.VISIBLE
                }
            } else {
                invoice_fragment_nao_processada.visibility = View.VISIBLE
            }
        }

        val shadowView: MaterialShadowContainerView = constraintLayout19
        val density = resources.displayMetrics.density
        shadowView.shadowTranslationZ = density * 3.0f
        shadowView.shadowElevation = density * 5.0f
        shadowView.setUseSpotShadow(true)

        setClickListener()
    }

    fun setObservers() {
        vModel!!.detailsOk.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (invoice_fragment_progress != null) {
                    invoice_fragment_progress.visibility = View.GONE
                    setadapter(vModel.transactions)
                }
                if (vModel.transactions.isEmpty()) {
                    invoice_fragment_fatura_vazia?.visibility = View.VISIBLE
                }
                (activity as InvoiceActivity).viewpager.isUserInputEnabled = true
                touchables.forEach { it.isEnabled = true }

            }
        })

        vModel.response1.observe(this, Observer {
            it?.let { response ->
                TODO()
            }
        })
    }

    fun setadapter(transactions: MutableList<Transaction>) {
        val recyclerView = activity!!.findViewById<RecyclerView>(R.id.invoice_fragment_sumary)
        adapter = InvoiceMenuAdapter(activity!!, transactions) {
            setBottomSheet(it)
            bottomSheet.show(activity!!.supportFragmentManager, bottomSheet.tag)

        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun setBottomSheet(transaction: Transaction) {
        val actions = arrayListOf<BottomSheetActions>(BottomSheetActions.COMPRA)
        bottomSheet = CustomBottomSheetDialogFragment.newInstance(actions)
        bottomSheet.run {
            companionTransaction.value = transaction
        }

        flag.observe(this, Observer {
            bottomSheet.dismiss()
        })
    }

    private fun setPaymentBottomSheet() {
        val actions = arrayListOf<BottomSheetActions>(BottomSheetActions.PAGAMENTO)
        bottomSheet = CustomBottomSheetDialogFragment.newInstance(actions)
        bottomSheet.run {
            companionSumary.value = invoiceSumary

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setClickListener() {
        invoice_fragment_baixar_fatura.setOnClickListener {
            checkIfAlreadyhavePermission(invoiceSumary.due.dueDate)
        }

        invoice_fragment_pagar_fatura.setOnClickListener {
            setPaymentBottomSheet()
            bottomSheet.show(activity!!.supportFragmentManager, bottomSheet.tag)
        }

        invoice_fragment_baixar.setOnClickListener {
            checkIfAlreadyhavePermission(invoiceSumary.due.dueDate)
        }
    }

    fun getType(type: String): String {
        return if (invoiceSumary.paid) {
            "Fatura paga"
        } else
            when (type.toLowerCase()) {
                "open" -> "Fatura aberta"
                "closed" -> {
                    "Fatura fechada"
                }

                else -> {
                    "Em processamento"
                }
            }
    }

    fun configureColor(type: String) {
        if (invoiceSumary.paid) {
            invoice_fragment_invoice_title.setTextColor(Color.parseColor("#39A500"))
        } else
            when (type.toLowerCase()) {
                "open" -> {
                    invoice_fragment_invoice_title.setTextColor(Color.parseColor("#4A4A4A"))
                }
                "closed" -> {
                    invoice_fragment_invoice_title.setTextColor(Color.parseColor("#00A7FF"))
                }
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun checkIfAlreadyhavePermission(dueDate: String) {
        val result = ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED) {
            vModel.downloadInvoice(dueDate, context)
        } else {
            ActivityCompat.requestPermissions((context as Activity?)!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    3232)
        }
    }

    override fun onResume() {
        setTouchables()
        (activity as InvoiceActivity).viewpager.isUserInputEnabled = ::adapter.isInitialized
        touchables.forEach { it.isEnabled = ::adapter.isInitialized }
        super.onResume()
        vModel.getDetails(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), MyApplication.preferences.getString("idAccount", null), invoiceSumary.due.dueDate)
    }

    override fun onPause() {
        super.onPause()
        vModel.cancelCall()
    }

    private fun setTouchables() {
        touchables = (activity as InvoiceActivity).tabLayout.touchables
    }
}

