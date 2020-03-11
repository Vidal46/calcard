package br.com.calcard.android.app.ui.insurance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.ActivityVehicleFormBinding
import br.com.calcard.android.app.model.Beneficiary
import br.com.calcard.android.app.model.Vehicle
import kotlinx.android.synthetic.main.activity_vehicle_form.*
import java.util.*

class VehicleFormActivity : AppCompatActivity() {
    private val activityContext = this@VehicleFormActivity
    lateinit var binding: ActivityVehicleFormBinding
    lateinit var vehicle: Vehicle
    private var insuranceId: Long = 0
    private var insurancePrice: Double = 0.0
    var beneficiaries: ArrayList<Beneficiary> = ArrayList()
    var insuranceName = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_form)

        intent.run {
            beneficiaries = getParcelableArrayListExtra("beneficiaries")
            insuranceId = getLongExtra("insuranceId", 0)
            insuranceName = getStringExtra("insuranceName")
            insurancePrice = getDoubleExtra("insurancePrice", insurancePrice)
        }
        setClickListener()
    }

    private fun setClickListener() {
        vehicle_form_button.setOnClickListener {
            buttonCheck()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun buttonCheck() {
        if (validateFields()) {
            buildVehicle()
            startActivity(Intent(this, CardPasswordActivity::class.java)
                    .putParcelableArrayListExtra("beneficiaries", beneficiaries)
                    .putExtra("insuranceId", insuranceId)
                    .putExtra("vehicle", vehicle)
                    .putExtra("insurancePrice",insurancePrice)
                    .putExtra("vehicleBoolean", true)
                    .putExtra("insuranceName", insuranceName))
        } else showToast("Favor preencher todos os campos corretamente")
    }

    private fun buildVehicle() {
        vehicle = Vehicle(
                binding.vehicleFormCorInput.editText?.text.toString(),
                "",
                binding.vehicleFormMarcaInput.editText?.text.toString(),
                binding.vehicleFormAnoFabInput.editText?.text.toString().toLong(),
                binding.vehicleFormAnoModeloInput.editText?.text.toString(),
                binding.vehicleFormAnoModeloInput.editText?.text.toString().toLong(),
                binding.vehicleFormPlacaInput.editText?.text.toString(),
                binding.vehicleFormChassiInput.editText?.text.toString()
        )
    }

    private fun validateFields(): Boolean {
        var valid = true

        if (binding.vehicleFormMarcaInput.editText?.text?.isEmpty()!!) valid = false

        if (binding.vehicleFormAnoModeloInput.editText?.text?.isEmpty()!!) valid = false

        if (binding.vehicleFormPlacaInput.editText?.text?.isEmpty()!!) valid = false

        if (binding.vehicleFormAnoFabInput.editText?.text?.isEmpty()!!) valid = false

        if (binding.vehicleFormAnoModeloInput.editText?.text?.isEmpty()!!) valid = false

        return valid
    }
}
