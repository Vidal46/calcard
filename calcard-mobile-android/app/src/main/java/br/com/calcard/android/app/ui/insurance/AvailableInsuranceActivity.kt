package br.com.calcard.android.app.ui.insurance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.adapter.AvailableInsurancesAdapter
import br.com.calcard.android.app.model.Contractable
import br.com.calcard.android.app.model.Insurance
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.activity_my_insurance.*

class AvailableInsuranceActivity : AppCompatActivity() {
    private val activityContext = this@AvailableInsuranceActivity
    private val contractedInsurances: MutableList<Insurance> = mutableListOf()
    lateinit var recyclerView: RecyclerView

    val viewModel: InsuranceViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_insurance)

        recyclerView = my_insurance_recycler
        setObservers()
        callList()
    }

    private fun setObservers() {
        viewModel.contactableInsurancesLiveData.observe(this, Observer {
            it.let {
                setAdapter(it!!)
                dismissProgress()
            }
        })

        viewModel.errorHandler.observe(this, Observer {
            showToast(it!!)
            dismissProgress()
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun dismissProgress() {
        my_insurance_progress.visibility = View.GONE
    }

    private fun callList() {
        viewModel.getAvailableInsurances(Constants.BEARER_API, MyApplication.preferences.getString("idAccount", "0")!!/*, "contractable"*/)
    }

    private fun openInsuranceDetails(insuranceId: Long, insuranceName: String, insurancePrice: Double) {
        startActivity(Intent(this, InsuranceDetailActivity::class.java)
                .putExtra("insuranceId", insuranceId)
                .putExtra("insuranceName", insuranceName)
                .putExtra("insurancePrice", insurancePrice))
    }

    private fun setAdapter(insurances: MutableList<Contractable>) {
        with(recyclerView) {
            adapter = AvailableInsurancesAdapter(activityContext, insurances) {
                openInsuranceDetails(it.id!!, it.name!!, it.price!!)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}
