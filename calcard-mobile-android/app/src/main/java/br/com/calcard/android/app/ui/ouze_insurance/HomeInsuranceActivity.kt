package br.com.calcard.android.app.ui.ouze_insurance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.adapter.AvailableInsurancesAdapter
import br.com.calcard.android.app.adapter.ContractedInsurancesAdapter
import br.com.calcard.android.app.model.Insurance
import br.com.calcard.android.app.ui.insurance.InsuranceDetailActivity
import br.com.calcard.android.app.ui.insurance.InsuranceViewModel
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.activity_home_insurance.*

class HomeInsuranceActivity : AppCompatActivity() {

    private val activityContext = this@HomeInsuranceActivity

    private lateinit var myInsurancesRecyclerView: RecyclerView

    private lateinit var availableInsurancesRecyclerView: RecyclerView

    private var insurance = MutableLiveData<Insurance>()

    private val viewModel: InsuranceViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_insurance)

        bindViews()
        setObservers()
        callInsurance()
    }

    private fun callInsurance() {
        viewModel.getAvailableInsurances(Constants.BEARER_API, MyApplication.preferences.getString("idAccount", "0")!!)
    }

    private fun setObservers() {

        viewModel.run {
            insuranceLiveData.observe(activityContext, Observer {
                insurance.value = it
            })

            errorHandler.observe(activityContext, Observer {
                showToast(it)
            })

            connectionError.observe(activityContext, Observer {
                showToast("Erro de conexão")
            })
        }

        insurance.observe(activityContext, Observer {
            setAvailableInsurancesAdapter()
            setMyInsurancesAdapter()
        })

    }

    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun bindViews() {

        availableInsurancesRecyclerView = home_insurance_my_insurances_recycler_recycler

        myInsurancesRecyclerView = available_insurance_my_insurances_recycler_recycler
    }

    private fun openInsuranceDetails(insuranceId: Long, insuranceName: String, insurancePrice: Double) {
        startActivity(Intent(this, InsuranceDetailActivity::class.java)
                .putExtra("insuranceId", insuranceId)
                .putExtra("insuranceName", insuranceName)
                .putExtra("insurancePrice", insurancePrice))
    }

    private fun setAvailableInsurancesAdapter() {
        availableInsurancesRecyclerView.run {
            adapter = AvailableInsurancesAdapter(activityContext, insurance.value?.contractable!!) {
                openInsuranceDetails(it.id!!, it.name!!, it.price!!)
            }

            layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)
        }.also {
            if (insurance.value?.contractable?.isEmpty()!!) {
                availableInsurancesRecyclerView.visibility = View.INVISIBLE
            }
        }
    }

    private fun setMyInsurancesAdapter() {
        myInsurancesRecyclerView.run {
            adapter = ContractedInsurancesAdapter(activityContext, insurance.value?.contracted!!) {

            }

            layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)
        }.also {
            if (insurance.value?.contracted?.isEmpty()!!) {
                myInsurancesRecyclerView.visibility = View.INVISIBLE
            }
        }
    }
}
