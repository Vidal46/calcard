package br.com.calcard.android.app.ui.insurance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.adapter.InsuranceDetailsAdapter
import br.com.calcard.android.app.model.Beneficiary
import br.com.calcard.android.app.model.Coverage
import br.com.calcard.android.app.model.InsuranceDetail
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.activity_insurance_detail.*
import java.util.*

class InsuranceDetailActivity : AppCompatActivity() {

    private val activityContext = this@InsuranceDetailActivity

    private var insuranceDetail = MutableLiveData<InsuranceDetail>()

    private var insuranceId: Long = 0

    private var insurancePrice: Double = 0.0

    var insuranceName = String()

    var beneficiaries: ArrayList<Beneficiary> = ArrayList()

    lateinit var recyclerView: RecyclerView

    lateinit var nextButton: Button

    val viewModel: InsuranceViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_detail)

        intent.run {
            insuranceId = getLongExtra("insuranceId", 0)
            insuranceName = getStringExtra("insuranceName")
            insurancePrice = getDoubleExtra("insurancePrice", 0.0)
        }

        bindViews()
        setObserver()
        setClickListener()
        callInsuranceDetails()
    }

    private fun bindViews() {
        recyclerView = insurance_detail_recycler
        nextButton = insurance_detail_accept_button
//        insurance_detail_value_text_value.text = "${FormatterUtil.formatCurrency(insurancePrice)}"
    }

    private fun callInsuranceDetails() {
        viewModel.getInsuranceDetail(Constants.BEARER_API, insuranceId)
    }

    private fun setClickListener() {
        insurance_detail_back_button.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setObserver() {
        viewModel.insuranceDetailLiveData.observe(this, Observer {
            setAdapter(it!!.coverages!!)
            insuranceDetail.value = it
            reactiveSetValue()
            dismissProgress()
        })

        viewModel.errorHandler.observe(this, Observer {
            showToast(it!!)
            dismissProgress()
        })
//
//        this.insurance_detail_checkbox.setOnClickListener {
//            changeVisibility(nextButton)
//        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun dismissProgress() {
        insurance_detail_progress.visibility = View.GONE
    }
//
//    private fun changeVisibility(view: View) {
//        if (view.visibility == View.VISIBLE) view.visibility = View.INVISIBLE
//        else view.visibility = View.VISIBLE
//    }

    private fun reactiveSetValue() {
        this.insuranceDetail.observe(this, Observer {
            insurance_detail_text.text = it?.name
        })

        when {
            insuranceDetail.value!!.beneficiary!! -> this.nextButton.setOnClickListener {
                startActivity(Intent(this, AddingBeneficiaryActivity::class.java)
                        .putExtra("beneficiary", insuranceDetail.value!!.beneficiary!!)
                        .putExtra("vehicle", insuranceDetail.value!!.vehicleData)
                        .putExtra("insuranceName", insuranceName)
                        .putExtra("insurancePrice", insurancePrice)
                        .putExtra("insuranceId", insuranceId))
            }
            insuranceDetail.value!!.vehicleData!! -> this.nextButton.setOnClickListener {
                startActivity(Intent(this, VehicleFormActivity::class.java)
                        .putExtra("vehicle", insuranceDetail.value!!.vehicleData)
                        .putExtra("beneficiaries", beneficiaries)
                        .putExtra("insuranceName", insuranceName)
                        .putExtra("insurancePrice", insurancePrice)
                        .putExtra("insuranceId", insuranceId))
            }
            !insuranceDetail.value!!.beneficiary!! && !insuranceDetail.value!!.vehicleData!! -> this.nextButton.setOnClickListener {
                startActivity(Intent(this, HiringInsuranceActivity::class.java)
                        .putExtra("beneficiary", insuranceDetail.value!!.beneficiary!!)
                        .putExtra("vehicle", insuranceDetail.value!!.vehicleData)
                        .putExtra("beneficiaries", beneficiaries)
                        .putExtra("insuranceName", insuranceName)
                        .putExtra("insurancePrice", insurancePrice)
                        .putExtra("insuranceId", insuranceId))
            }
        }
    }

    private fun setAdapter(coverages: MutableList<Coverage>) {
        with(recyclerView) {
            adapter = InsuranceDetailsAdapter(activityContext, coverages)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}
