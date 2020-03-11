package br.com.calcard.android.app.ui.insurance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.calcard.android.app.R
import kotlinx.android.synthetic.main.activity_successful_hiring.*

class SuccessfulHiringActivity : AppCompatActivity() {
    var cancel = false
    var insuranceName = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_hiring)

        intent.run {
            cancel = getBooleanExtra("cancel", false)
            insuranceName = getStringExtra("insuranceName")
        }

        setSuccessfulText()
        setClickListener()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MyInsurancesActivity::class.java))
    }

    private fun setClickListener() {
        successful_hiring_button.setOnClickListener {
            finishFlux()
        }
    }

    private fun finishFlux() {
        startActivity(Intent(this, MyInsurancesActivity::class.java))
    }

    private fun setSuccessfulText() {
        if (cancel) {
            successful_hiring_title.text = "Seguros e Assistências"
            successful_hiring_text.text = "Seu plano $insuranceName foi cancelado com sucesso!"
        } else {
            successful_hiring_text.text = "Você contratou o $insuranceName\n com sucesso!"
        }
    }

}
