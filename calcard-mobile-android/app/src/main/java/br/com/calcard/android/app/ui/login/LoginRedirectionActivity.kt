package br.com.calcard.android.app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.redirection.MainRedirectionActivity
import br.com.calcard.android.app.utils.CaseManipulation
import kotlinx.android.synthetic.main.activity_login_redirection.*

class LoginRedirectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_redirection)


        if (MyApplication.preferences.contains("cpf")) {
            login_redirection_cpf.text = "CPF: ${MyApplication.preferences.getString("cpf", "")}"

        } else {
            intent = Intent(this, MainRedirectionActivity::class.java).putExtra("cpf", getCpf())
            MyApplication.preferences.edit().putString("name", null).apply()
            MyApplication.preferences.edit().putString("cpf", null).apply()
            startActivity(intent)
            finish()
        }
        if (MyApplication.preferences.contains("name")) login_redirection_title.text = "Olá, ${CaseManipulation.toCamelCase(MyApplication.preferences.getString("name", ""))}!"


    }

    fun entrar(view: View) {
        intent = Intent(this, LoginActivity::class.java).putExtra("cpf", getCpf())
        startActivity(intent)
        finish()
    }

    fun entrarComOutraConta(view: View) {
        intent = Intent(this, MainRedirectionActivity::class.java).putExtra("cpf", getCpf())
        MyApplication.preferences.edit().putString("name", null).apply()
        MyApplication.preferences.edit().putString("cpf", null).apply()
        startActivity(intent)
        finish()
    }

    fun getCpf(): String? {
        val sharedPreferences = this.getSharedPreferences("cpf", Context.MODE_PRIVATE)
        val shared = sharedPreferences.getString("cpf", "")
        return shared
    }

    override fun onBackPressed() {
        onBackPressedActivity()
    }

    private fun onBackPressedActivity() {
        startActivity(Intent(this, MainRedirectionActivity::class.java))
        finish()
    }

}
