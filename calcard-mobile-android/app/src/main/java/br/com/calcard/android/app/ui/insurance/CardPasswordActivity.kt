package br.com.calcard.android.app.ui.insurance

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.ActivityCardPasswordBinding
import br.com.calcard.android.app.model.Beneficiary
import br.com.calcard.android.app.model.RequestSaleInsuranceDTO
import br.com.calcard.android.app.model.Vehicle
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.activity_card_password.*
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class CardPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityCardPasswordBinding
    lateinit var beneficiaries: List<Beneficiary>
    lateinit var requestSaleInsuranceDTO: RequestSaleInsuranceDTO
    private var publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqfEwu2cxg8PUmOctzRbjMAFvhXwakxrWky1jAhmkRLmevGxc+b2kay/9/AiaRL3Exw6GL7SXo0Y2Hr1t6ODc6CwYOyVPeVPbx8za2inJSOxqT6GxOcrQuzhxOyF7jlbaAxQK+IEUsHhBKiE4Vd8J52RmJ1rzEYFiori8jbNUIOrM6WhloLdv0XGaVesToP9rD3kCbcRCWS7iXmHuisfMvHhTQLCGt2t0Cotu+1/+vGixDMXvDFbMrK0TWFF+3k+VZwSyBY5IKJdbBbj1ET/M7iocLOLXiCiIPLvRkue7v6ykKsMLNP23XJ/8n0+XXXzx5YjtGDgnpCcxG/PGXBxBawIDAQAB"
    private val activityContext = this@CardPasswordActivity
    private var insuranceId: Long = 0
    private var vehicle: Vehicle? = null
    private var vehicleBoolean = false
    var insuranceName = String()
    var encryptedPassword = String()

    val viewModel: InsuranceViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.run {
            beneficiaries = getParcelableArrayListExtra("beneficiaries")
            insuranceId = getLongExtra("insuranceId", 0)
            insuranceName = getStringExtra("insuranceName")
            vehicleBoolean = getBooleanExtra("vehicleBoolean", false)
        }

        binding = DataBindingUtil.setContentView(activityContext, R.layout.activity_card_password)
        checkVehicle()
        setObserver()
        setClickListener()
        reactiveContract()
    }

    private fun setObserver() {
        viewModel.errorHandler.observe(this, Observer {
            showToast(it!!)
            dismissProgress()
        })
    }

    private fun showProgress() {
        password_insurance_progress.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        password_insurance_progress.visibility = View.GONE
    }

    private fun checkVehicle() {
        if (vehicleBoolean) intent.run {
            vehicle = getSerializableExtra("vehicle") as Vehicle
        }
    }

    private fun encryptPlainPassword() {
        encryptedPassword = encrypt(binding.passwordInsuranceValue.text.toString())
    }

    private fun buildDto() {
        encryptPlainPassword()
        requestSaleInsuranceDTO = RequestSaleInsuranceDTO(MyApplication.preferences.getString("idAccount", "0")!!.toLong(), beneficiaries, MyApplication.preferences.getString("card", "0").toLong(), insuranceId, removeN(encryptedPassword), vehicle)
    }

    private fun setClickListener() {
        binding.myInsuranceButton.setOnClickListener {
            showProgress()
            validateFields()
        }
    }

    private fun validateFields() {
        if (binding.passwordInsuranceValue.text.trim().isNotEmpty()) contractInsurance()
        else {
            showToast("Todos os campos devem estar preenchidos")
            dismissProgress()
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show()
    }

    private fun contractInsurance() {
        buildDto()
        viewModel.contractInsurance(Constants.BEARER_API, requestSaleInsuranceDTO)
    }

    private fun reactiveContract() {
        viewModel.wellContractedInsuranceLiveData.observe(this, Observer {
            if (it!!) nextActivity()
            else showToast("problema na contratação do seguro")
            dismissProgress()
        })
    }

    private fun nextActivity() {
        startActivity(Intent(this, SuccessfulHiringActivity::class.java)
                .putExtra("insuranceName", insuranceName)
                .putExtra("type", "contract"))
    }

    private fun getPublicKey(): PublicKey {
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(X509EncodedKeySpec(Base64.decode(publicKey.toByteArray(), Base64.DEFAULT)))
    }

    private fun encrypt(data: String): String {
        return try {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(1, getPublicKey())
            Base64.encodeToString(cipher.doFinal(data.toByteArray()), Base64.DEFAULT)
        } catch (var2: Exception) {
            throw Exception(var2)
        }
    }

    private fun removeN(value: String?): String {
        return value?.replace("\n", "") ?: ""
    }

}
