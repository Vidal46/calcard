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
import br.com.calcard.android.app.adapter.ContractedInsurancesAdapter
import br.com.calcard.android.app.model.Contracted
import br.com.calcard.android.app.model.Insurance
import br.com.calcard.android.app.ui.cards.HomeActivity
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.activity_my_insurances_activiy.*

class MyInsurancesActivity : AppCompatActivity() {
    private val activityContext = this@MyInsurancesActivity

    lateinit var recyclerView: RecyclerView

    lateinit var insurance: Insurance

    var insurances: MutableList<Contracted> = mutableListOf()

    val viewModel: InsuranceViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_insurances_activiy)

        setObservers()
        bindViews()
        callContractedList()
//        setClickListener()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun checkTitle() {
        if (insurances.size < 1) {
            my_insurances_text.visibility = View.GONE
        }
    }

    private fun bindViews() {
        recyclerView = my_insurances_recycler_recycler
    }

    private fun checkVisibilities() {
        if (insurances.size == 0) {
            my_insurances_recycler_container.visibility = View.GONE
        } else {
            my_insurances_recycler_container.visibility = View.VISIBLE
        }
    }

    private fun dismissProgress() {
        my_insurances_progress.visibility = View.GONE
    }

    private fun callContractedList() {
        viewModel.getAvailableInsurances(Constants.BEARER_API, MyApplication.preferences.getString("idAccount", "0")!!)
    }

//    private fun setClickListener() {
//        my_insurance_button.setOnClickListener {
//            showAvailableInsurances()
//        }
//    }

    private fun showAvailableInsurances() {
        if (insurance.contractable!!.size > 0) {
            startActivity(Intent(this, AvailableInsuranceActivity::class.java))
        } else showToast("Não existem seguros disponíveis para contratação")
    }

    private fun setObservers() {
        viewModel.contractedInsurancesLiveData.observe(this, Observer {
            setAdapter(it!!)
            insurances = it
            checkTitle()
            checkVisibilities()
            dismissProgress()
        })

        viewModel.insuranceLiveData.observe(this, Observer {
            insurance = it!!
        })

        viewModel.errorHandler.observe(this, Observer {
            showToast(it!!)
            dismissProgress()
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun cancelInsurance(insuranceId: Long) {
        startActivity(Intent(this, CancelInsuranceActivity::class.java).putExtra("insuranceId", insuranceId))
    }


    private fun setAdapter(insurances: MutableList<Contracted>) {
        with(recyclerView) {
            adapter = ContractedInsurancesAdapter(activityContext, insurances) {
                cancelInsurance(it.id!!)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
