package br.com.calcard.android.app.ui.redirection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.register.RegisterViewModel
import br.com.calcard.android.app.ui.smsverification.OuzeSmsConfirmationActivity
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.activity_registration_middle_ware.*

class RegistrationMiddleWareActivity : AppCompatActivity() {

    lateinit var cpfIntent: String
    var registrationViewModel = RegisterViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_middle_ware)
        intent.run {
            cpfIntent = getStringExtra("cpf")
        }

        setClickListener()
    }

    private fun setClickListener() {
        registrationMiddleWare_button.setOnClickListener {
            registrationMiddleWare_progress.visibility = View.VISIBLE
            redirectToRegistration()
        }
    }

    override fun onBackPressed() {
        onBackPressedActivity()
    }

    private fun onBackPressedActivity() {
        startActivity(Intent(this, MainRedirectionActivity::class.java))
        finish()
    }


    private fun redirectToRegistration() {
        registrationViewModel.generateToken(Constants.BEARER_API, cpfIntent, "REGISTER")

        registrationViewModel.errorHandler.observe(this, Observer {
            registrationMiddleWare_progress.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        registrationViewModel.cpfObserver.observe(this, Observer {
            intent = Intent(this, OuzeSmsConfirmationActivity::class.java)
            intent.run {
                putExtra("phone", registrationViewModel.phone.get())
                putExtra("cpf", cpfIntent)
            }

            registrationMiddleWare_progress.visibility = View.GONE
            startActivity(intent)
        })
    }
}
