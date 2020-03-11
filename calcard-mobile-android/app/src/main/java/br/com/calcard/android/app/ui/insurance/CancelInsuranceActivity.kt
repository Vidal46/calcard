package br.com.calcard.android.app.ui.insurance

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.adapter.HiringInsuranceDetailsAdapter
import br.com.calcard.android.app.model.Coverage
import br.com.calcard.android.app.model.SaleInsurance
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.FormatterUtil
import kotlinx.android.synthetic.main.activity_cancel_insurance.*

class CancelInsuranceActivity : AppCompatActivity() {
    private val activityContext = this@CancelInsuranceActivity
    private val activityLayoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.VERTICAL, false)
    lateinit var inflater: View
    lateinit var alertDialog: AlertDialog
    private var insuranceId: Long = 0
    private var insuranceRealId: Long = 0
    private var insurancePrice: Double = 0.0
    private val cancelInsuranceDetail = MutableLiveData<SaleInsurance>()
    lateinit var recyclerView: RecyclerView

    val viewModel: InsuranceViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_insurance)

        intent.run {
            insuranceId = getLongExtra("insuranceId", 0)
        }

        callInsuranceDetails()
        reactStartActivity()
        reactiveSetValue()
        getInsuranceId()
        setClickListener()
        setObserver()
        bindView()
    }

    private fun getContract() {
        viewModel.getInsuranceContract(insuranceRealId, activityContext)
    }

    private fun getInsuranceId() {
        viewModel.getInsuranceId(Constants.BEARER_API, insuranceId)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show()
            getContract()
        } else {
            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfAlreadyPermission() {
        val result = ContextCompat.checkSelfPermission(activityContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED) {
            getContract()
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 3232)
        }
    }

    private fun setObserver() {
        viewModel.cancelInsuranceDetailLiveData.observe(this, Observer {
            setAdapter(it!!.insurance!!.coverages!!)
            cancelInsuranceDetail.value = it
            dismissProgress()
        })

        viewModel.insuranceContractPdfLiveData.observe(this, Observer {
            if (it!!) dismissProgress()
        })

        viewModel.fullInsuranceLiveData.observe(this, Observer {
            insuranceRealId = it!!.insurance!!.id!!
            insurancePrice = it!!.insurance!!.price!!

            cancel_insurance_value_text_value.text = "${FormatterUtil.formatCurrency(insurancePrice)}"
        })

        viewModel.errorHandler.observe(this, Observer {
            showToast(it!!)
        })
    }

    private fun setClickListener() {
        cancel_insurance_button.setOnClickListener {
            showDialog()
        }

        cancel_insurance_link.setOnClickListener {
            showProgress()
            checkIfAlreadyPermission()
        }
    }

    private fun showProgress() {
        cancel_insurance_progress.visibility = View.VISIBLE
    }


    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun dismissProgress() {
        cancel_insurance_progress.visibility = View.GONE
    }

    private fun bindView() {
        recyclerView = cancel_insurance_recycler
    }

    private fun reactiveSetValue() {
        this.cancelInsuranceDetail.observe(this, Observer {
            cancel_insurance_text.text = it!!.insuranceName
        })
    }

    private fun reactStartActivity() {
        viewModel.cancelInsuranceFlagLiveData.observe(this, Observer {
            if (it!!) startActivity(Intent(this, SuccessfulHiringActivity::class.java)
                    .putExtra("cancel", true)
                    .putExtra("insuranceName", cancelInsuranceDetail.value!!.insuranceName))
            else showToast("Erro ao deletar seguro")
        })
    }

    private fun callInsuranceDetails() {
        viewModel.getInsuranceSaleDetail(Constants.BEARER_API, insuranceId)
    }

    private fun showDialog() {
        inflater = layoutInflater.inflate(R.layout.cancel_insurance_dialog, null)

        val builder = AlertDialog.Builder(activityContext)
        val cancelButton = inflater.findViewById<Button>(R.id.cancel_insurance_dialog_button)
        val cancelLink = inflater.findViewById<TextView>(R.id.cancel_insurance_dialog_link)
        builder.setView(inflater).create()
        alertDialog = builder.show()
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        cancelLink.setOnClickListener {
            deleteInsurance()
        }
    }

    private fun deleteInsurance() {
        viewModel.deleteInsurance(Constants.BEARER_API, MyApplication.preferences.getString("idAccount", "0")!!, insuranceId)
    }

    private fun setAdapter(coverages: MutableList<Coverage>) {
        with(recyclerView) {
            adapter = HiringInsuranceDetailsAdapter(activityContext, coverages, true)
            layoutManager = activityLayoutManager
        }
    }

}

