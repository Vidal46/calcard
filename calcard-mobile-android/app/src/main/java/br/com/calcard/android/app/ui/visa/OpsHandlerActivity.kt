package br.com.calcard.android.app.ui.visa

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.redirection.MainRedirectionActivity
import kotlinx.android.synthetic.main.activity_ops_handler.*
import kotlinx.android.synthetic.main.activity_ops_handler.ops_no_migracao_title
import kotlinx.android.synthetic.main.ops_no_migracao.*

class OpsHandlerActivity : AppCompatActivity() {

    lateinit var errorCodeExtra: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ops_handler)
        var errorMessenge = String()

        intent.run {
            errorCodeExtra = getStringExtra("erro")
        }

        when (errorCodeExtra) {
            "406" -> errorMessenge = "Infelizmente no momento você não é elegível para migração. Para mais informações entre em contato com a Ouze para saber sua situação."
            "400" -> errorMessenge = "No momento a migração não está disponível para sua conta. Em breve, tente novamente."
            "424" -> errorMessenge = "Você é elegível para a migração! Por problemas nos seus dados precisamos que você faça o processo em uma das nossas lojas Studio Z."
            "409" -> errorMessenge = "Você já possui o nosso cartão Ouze Visa, aproveite todas as vantagens e o nosso aplicativo."
            "503" -> errorMessenge = "Erro interno."
            "451" -> errorMessenge = "Sua migração está completa, você deve ir até uma loja Studio Z para pegar seu cartão."
        }

        if (errorCodeExtra.equals("451") || errorCodeExtra.equals("409")) {
            ops_no_migracao_image.visibility = View.INVISIBLE
            ops_no_migracao_title.text = "Parabéns!"
        }

        ops_no_migracao_text.text = errorMessenge

        ops_no_migracao_button.setOnClickListener {
            finishMigration()
        }
    }

    fun finishMigration() {
        finish()
        startActivity(Intent(this, MainRedirectionActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
