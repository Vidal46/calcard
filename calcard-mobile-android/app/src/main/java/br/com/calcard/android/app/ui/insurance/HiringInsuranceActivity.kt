package br.com.calcard.android.app.ui.insurance

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.adapter.HiringInsuranceDetailsAdapter
import br.com.calcard.android.app.model.Beneficiary
import br.com.calcard.android.app.model.Coverage
import br.com.calcard.android.app.model.InsuranceDetail
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.FormatterUtil
import kotlinx.android.synthetic.main.activity_hiring_insurance.*
import java.util.*

class HiringInsuranceActivity : AppCompatActivity() {
    private val activityContext = this@HiringInsuranceActivity
    private val hiringInsuranceDetail = MutableLiveData<InsuranceDetail>()
    var beneficiaries: ArrayList<Beneficiary> = ArrayList()
    lateinit var recyclerView: RecyclerView
    private var insuranceId: Long = 0
    private var insurancePrice: Double = 0.0
    lateinit var nextButton: Button
    var insuranceName = String()

    val viewModel: InsuranceViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiring_insurance)

        intent.run {
            insuranceId = getLongExtra("insuranceId", 0)
            beneficiaries = getParcelableArrayListExtra("beneficiaries")
            insuranceName = getStringExtra("insuranceName")
            insurancePrice = getDoubleExtra("insurancePrice", 0.0)
        }

        hiring_insurance_value_text_value.text = "${FormatterUtil.formatCurrency(insurancePrice)}"

        callInsuranceDetails()
        setClickListener()
        reactiveSetValue()
        setObservers()
        bindViews()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show()
            getContract()
        } else {
            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dismissProgress() {
        hiring_insurance_progress.visibility = View.GONE
    }

    private fun setObservers() {
        viewModel.insuranceDetailLiveData.observe(this, Observer {
            setAdapter(it!!.coverages!!)
            hiringInsuranceDetail.value = it
            dismissProgress()
        })

        viewModel.insuranceContractPdfLiveData.observe(this, Observer {
            if (it!!) dismissProgress()
        })

        viewModel.errorHandler.observe(this, Observer {
            showToast(it!!)
            dismissProgress()
        })
    }

    private fun reactiveSetValue() {
        this.hiringInsuranceDetail.observe(this, Observer {
            hiring_insurance_text.text = it?.name
        })
    }

    private fun getContract() {
        viewModel.getInsuranceContract(insuranceId, activityContext)
    }

    private fun setClickListener() {
        hiring_insurance_button.setOnClickListener {
            startActivity(Intent(this, CardPasswordActivity::class.java)
                    .putParcelableArrayListExtra("beneficiaries", beneficiaries)
                    .putExtra("insuranceName", insuranceName)
                    .putExtra("insuranceId", insuranceId))
        }

        hiring_insurance_link.setOnClickListener {
            showProgress()
            checkIfAlreadyPermission()
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

    private fun bindViews() {
        recyclerView = hiring_insurance_recycler
        nextButton = hiring_insurance_button
    }

    private fun callInsuranceDetails() {
        viewModel.getInsuranceDetail(Constants.BEARER_API, insuranceId)
    }

    private fun showProgress() {
        hiring_insurance_progress.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun setAdapter(coverages: MutableList<Coverage>) {
        with(recyclerView) {
            adapter = HiringInsuranceDetailsAdapter(activityContext, coverages, false)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}
