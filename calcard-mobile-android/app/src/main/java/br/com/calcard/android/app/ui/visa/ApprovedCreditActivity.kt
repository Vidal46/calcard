package br.com.calcard.android.app.ui.visa

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.ActivityApprovedCreditBinding
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.utils.FormatterUtil
import kotlinx.android.synthetic.main.activity_approved_credit.*

class ApprovedCreditActivity : AppCompatActivity() {

    lateinit var binding: ActivityApprovedCreditBinding
    var extraObj = MigrationElegible()
    var cpfIntent = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approved_credit)
        intent.run {
            extraObj = getSerializableExtra("migrationEdible") as MigrationElegible
            cpfIntent = getStringExtra("cpf")
        }


        visa_approved_credit_accept_button.setOnClickListener {
            visa_approved_credit_progress.visibility = View.GONE
            intent = Intent(this, OuzePasswordActivity::class.java)
            intent.putExtra("migrationEdible", extraObj)
            intent.putExtra("cpf", cpfIntent)
            startActivity(intent)
        }


        visa_approved_credit_current_value.text = FormatterUtil.formatCurrency(extraObj.plLimit)
        visa_approved_credit_visa_value.text = FormatterUtil.formatCurrency(extraObj.visaLimit)

        visa_third_step_photo_no_button.setOnClickListener {
            visa_approved_credit_progress.visibility = View.GONE
            intent = Intent(this, LimitRefusementActivity::class.java)
            startActivity(intent)
        }
    }

}
