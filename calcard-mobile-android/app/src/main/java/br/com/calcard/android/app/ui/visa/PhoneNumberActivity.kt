package br.com.calcard.android.app.ui.visa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.MigrationElegible
import kotlinx.android.synthetic.main.activity_phone_number.*

class PhoneNumberActivity : AppCompatActivity() {

    lateinit var password: String
    lateinit var rsa: String

    var cpfIntent = String()
    var senha = String()
    var encrypted = String()
    var extraObj = MigrationElegible()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)

        intent.run {
            password = getStringExtra("senha")
            rsa = getStringExtra("encrypted")
            encrypted = getStringExtra("encrypted")
            cpfIntent = getStringExtra("cpf")
            senha = getStringExtra("senha")
            extraObj = getSerializableExtra("migrationEdible") as MigrationElegible
        }

        bindText()
        setClickListener()
    }

    private fun bindText() {
        phone_number_activity_text.text = "Foi enviado um código via SMS para o número ${extraObj.phone}. Avance e informe o código recebido para continuar."
    }

    private fun setClickListener() {
        phone_number_activity_button.setOnClickListener {
            intent = Intent(this, SmsCodeActivity::class.java)
            intent.putExtra("encrypted", encrypted)
            intent.putExtra("senha", senha)
            intent.putExtra("migrationEdible", extraObj)
            intent.putExtra("cpf", cpfIntent)
            startActivity(intent)
        }
    }
}
