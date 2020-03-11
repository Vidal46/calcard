package br.com.calcard.android.app.ui.insurance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.adapter.BeneficiaryAdapter
import br.com.calcard.android.app.databinding.ActivityAddingBeneficiaryBinding
import br.com.calcard.android.app.model.Beneficiary
import kotlinx.android.synthetic.main.activity_adding_beneficiary.*
import kotlinx.android.synthetic.main.insurance_beneficiary_dialog.view.*
import java.util.*

class AddingBeneficiaryActivity : AppCompatActivity() {

    private val activityContext = this@AddingBeneficiaryActivity
    private val activityLayoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.VERTICAL, false)
    lateinit var binding: ActivityAddingBeneficiaryBinding
    lateinit var recyclerView: RecyclerView
    lateinit var inflater: View
    lateinit var alertDialog: AlertDialog
    var beneficiaries: MutableList<Beneficiary> = mutableListOf()
    var serializableBeneficiaries: ArrayList<Beneficiary> = ArrayList()
    var beneficiaryBoolean: Boolean = true
    var beneficiaryToogleBoolean = true
    var vehicleBoolean: Boolean = false
    var percentageAmmount: Long = 0
    var insuranceName = String()
    var insuranceId: Long = 0
    var insurancePrice: Double = 0.0
    val maxAmmountValue: Long = 100

    val viewModel: InsuranceViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adding_beneficiary)

        intent.run {

            insuranceId = getLongExtra("insuranceId", insuranceId)
            vehicleBoolean = getBooleanExtra("vehicle", false)
            insuranceName = getStringExtra("insuranceName")
            insurancePrice = getDoubleExtra("insurancePrice", 0.0)
        }

        recyclerView = adding_beneficiary_recycler

        setAdapter(beneficiaries)
        checkVisibilities()
        setClickListeners()
    }

    private fun changeVisibility(view: View) {
        if (view.visibility == View.VISIBLE) view.visibility = View.GONE
        else view.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun getPercentageValues(): Long {
        percentageAmmount = 0
        beneficiaries.forEach {
            percentageAmmount += it.percentage!!
        }
        return percentageAmmount
    }

    private fun checkVisibilities() {
        if (beneficiaries.size == 0) {
            recyclerView.visibility = View.GONE
            adding_beneficiary_text.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            adding_beneficiary_text.visibility = View.GONE
        }
    }

    private fun setClickListeners() {
        adding_beneficiary_link.setOnClickListener {
            showDialog()
        }

        adding_beneficiary_button.setOnClickListener {
            checkValuesAndRequest()
        }
    }

    private fun checkValuesAndRequest() {
        if (getPercentageValues() != maxAmmountValue && beneficiaryBoolean) {
            if (beneficiaries.size == 0) showToast("É necessário adicionar ao menos 1 beneficiário")
            else showToast("Os percentuais dos beneficiários devem somar 100%")
        } else {
            nextActivity()
        }
    }

    private fun nextActivity() {
        arrayToArray(beneficiaries)
        if (vehicleBoolean) {
            startActivity(Intent(this, VehicleFormActivity::class.java)
                    .putParcelableArrayListExtra("beneficiaries", serializableBeneficiaries)
                    .putExtra("insuranceId", insuranceId)
                    .putExtra("insurancePrice", insurancePrice)
                    .putExtra("insuranceName", insuranceName))
        } else {
            startActivity(Intent(this, HiringInsuranceActivity::class.java)
                    .putParcelableArrayListExtra("beneficiaries", serializableBeneficiaries)
                    .putExtra("insuranceId", insuranceId)
                    .putExtra("insurancePrice", insurancePrice)
                    .putExtra("insuranceName", insuranceName))
        }
    }

    private fun arrayToArray(list: List<Beneficiary>) {
        list.forEach {
            serializableBeneficiaries.add(it)
        }
    }

    private fun validateFields(): Boolean {
        val firstInput = inflater.insurance_beneficiary_input
        val secondInput = inflater.insurance_beneficiary_percentage_input
        return if (firstInput?.editText?.text?.isNotEmpty()!! && secondInput?.editText?.text?.isNotEmpty()!!) {
            if (secondInput.editText?.text?.toString()?.toDouble()!! > 100) {
                showToast("Valor não pode ultrapassar 100%")
                false
            } else {
                val newBeneficiary = Beneficiary(firstInput.editText?.text?.toString(), secondInput.editText?.text?.toString()?.toLong())
                beneficiaries.add(newBeneficiary)
                recyclerView.adapter?.notifyDataSetChanged()
                if (beneficiaries.size > 0) beneficiaryBoolean = true
                true
            }
        } else {
            showToast("Preencher todos os campos")
            false
        }
    }

    private fun showDialog() {
        if (beneficiaries.size < 6) {
            inflater = layoutInflater.inflate(R.layout.insurance_beneficiary_dialog, null)

            val builder = AlertDialog.Builder(activityContext)
            val saveButton = inflater.findViewById<Button>(R.id.insurance_beneficiary_button)
            builder.setView(inflater).create()
            alertDialog = builder.show()
            saveButton.setOnClickListener {
                if (validateFields()) {
                    alertDialog.dismiss()
                    checkVisibilities()
                }
            }
        } else {
            showToast("Você pode adicionar no máximo 6 beneficiários")
        }
    }

    private fun editBeneficiary(beneficiary: Beneficiary) {
        inflater = layoutInflater.inflate(R.layout.insurance_beneficiary_dialog, null)

        val builder = AlertDialog.Builder(activityContext)
        val saveButton = inflater.findViewById<Button>(R.id.insurance_beneficiary_button)
        builder.setView(inflater).create()
        val firstInput = inflater.insurance_beneficiary_input
        val secondInput = inflater.insurance_beneficiary_percentage_input
        firstInput.editText?.setText(beneficiary.name)
        secondInput.editText?.setText(beneficiary.percentage.toString())
        alertDialog = builder.show()
        saveButton.setOnClickListener {
            if (validateFields()) {
                beneficiaries.remove(beneficiary)
                alertDialog.dismiss()
                checkVisibilities()
            }
        }
    }

    private fun setAdapter(beneficiaries: MutableList<Beneficiary>) {
        with(recyclerView) {
            adapter = BeneficiaryAdapter(activityContext, beneficiaries, {
                beneficiaries.remove(it)
                recyclerView.adapter?.notifyDataSetChanged()
                if (!beneficiaryToogleBoolean && beneficiaryBoolean && beneficiaries.size == 0) {
                    beneficiaryBoolean = false
                }
                percentageAmmount = 0
                if (beneficiaries.size == 0) changeVisibility(adding_beneficiary_text)
            }) {
                editBeneficiary(it)
                percentageAmmount = 0
            }
            layoutManager = activityLayoutManager
        }
    }
}
