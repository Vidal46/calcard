package br.com.calcard.android.app.ui.redirection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.ui.visa.ConfirmIdentityActivity
import br.com.calcard.android.app.ui.visa.OpsHandlerActivity
import br.com.calcard.android.app.ui.visa.VisaMigrationViewModel
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.activity_error_handler.*

class ErrorHandlerActivity : AppCompatActivity() {

    lateinit var errorCodeExtra: String
    var elegibleCpf = MutableLiveData<MigrationElegible>()
    var migrationViewModel = VisaMigrationViewModel()
    var cpfIntent = String()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_handler)
        var errorMessenge = String()

        intent.run {
            errorCodeExtra = getStringExtra("codigo")
        }

        when (errorCodeExtra) {
            "primeiro" -> errorMessenge = "Você não possui um cartão Ouze Visa, vá a uma loja Studio Z, faça seu cartão e aproveite as vantagens!"
            "segundo" -> errorMessenge = "Você já é cliente, porém não possui cartão Ouze Visa. Clique em continuar para verificar o status da sua migração."
        }

        eror_handler_text.text = errorMessenge

        when (errorCodeExtra) {
            "segundo" -> {
                cpfIntent = intent.getStringExtra("cpf")
                error_handler_button.setOnClickListener {
                    redirectToMigration()
                    finish()
                }
            }
            "primeiro" -> {
                error_handler_button.text = "Voltar"
                error_handler_button.setOnClickListener {
                    var intent = Intent(this, MainRedirectionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        onBackPressedActivity()
    }

    private fun onBackPressedActivity() {
        startActivity(Intent(this, MainRedirectionActivity::class.java))
        finish()
    }

    private fun redirectToMigration() {
        error_handler_progress.visibility = View.VISIBLE
        migrationViewModel.getCpfEdible(Constants.BEARER_API, cpfIntent)

        migrationViewModel.cpfEnable.observe(this, Observer {
            elegibleCpf.value = it
            intent = Intent(this, ConfirmIdentityActivity::class.java)
            intent.putExtra("migrationEdible", elegibleCpf.value)
            intent.putExtra("cpf", cpfIntent)
            startActivity(intent)
            error_handler_progress.visibility = View.GONE
        })

        migrationViewModel.errorHandler.observe(this, Observer {
            error_handler_progress.visibility = View.GONE
            when (it) {
                "1000" -> {
                    Toast.makeText(this, "Favor aguardar dois minutos antes de pedir um novo código.", Toast.LENGTH_LONG).show()
                }
                "10000" -> {
                    Toast.makeText(this, "Você já superou o máximo de tentativas de biometria, para a sua segurança, favor dirigir-se à loja mais próxima para efetuar a migração.", Toast.LENGTH_LONG).show()
                }
                "10001" -> {
                    Toast.makeText(this, "Validação biométrica expirou, favor tirar nova foto.", Toast.LENGTH_LONG).show()
                }
                "10002" -> {
                    Toast.makeText(this, "Nenhum telefone celular encontrado na base de dados, favor atualizar seu cadastro junto à Ouze Calcard pelo número 0800 653 033.", Toast.LENGTH_LONG).show()
                }
                "100000" -> {
                    Toast.makeText(this, "Você já superou o máximo de envios de token, favor dirigir-se à loja mais próxima para efetuar a migração.", Toast.LENGTH_LONG).show()
                }
                "100001" -> {
                    Toast.makeText(this, "Token inválido ou já expirado.", Toast.LENGTH_LONG).show()
                }
                else -> onFailMigration(it!!)
            }
        })
    }

    private fun onFailMigration(errorCode: String) {
        intent = Intent(this, OpsHandlerActivity::class.java)
        intent.putExtra("erro", errorCode)
        startActivity(intent)
        error_handler_progress.visibility = View.GONE
    }

}
