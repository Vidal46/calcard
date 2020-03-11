package br.com.calcard.android.app.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.ActivityOuzeRegisterPasswordBinding
import br.com.calcard.android.app.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_ouze_register_password.*

class OuzeRegisterPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityOuzeRegisterPasswordBinding
    private var activityContext = this@OuzeRegisterPasswordActivity
    private var checkBool = false
    private var password = String()
    private var firstPassword = String()
    private var secondPassword = String()
    private var cpfIntent = String()
    private var code = String()
    private var isforgot: Boolean? = null

    val viewModel: InputDataViewModel by lazy {
        ViewModelProviders.of(activityContext).get(InputDataViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ouze_register_password)

        intent.run {
            cpfIntent = getStringExtra("cpf")
            code = getStringExtra("tokenSMS")
            isforgot = getBooleanExtra("isForgot", true)
        }

        setObservers()
        bindViews()
    }

    private fun checkRegistration() {
        @SuppressLint("HardwareIds") val android_id = Settings.Secure.getString(activityContext.contentResolver,
                Settings.Secure.ANDROID_ID)

        if (isforgot!!) {
            viewModel.forgot(android_id, cpfIntent, code, secondPassword)
        } else {
            viewModel.register(android_id, cpfIntent, code, secondPassword)
        }

    }

    private fun bindViews() {
        binding.button2.setOnClickListener {
            if (!checkBool) code(1)
            else code(2)

            checkIfChangePasswordVisibility()
        }
    }

    private fun setObservers() {
        viewModel.registerSuccess.observe(this, Observer {
            dismissProgress()
            showCheck()
            Handler().postDelayed({
                val intent = Intent(activityContext, LoginActivity::class.java).putExtra("cpf", cpfIntent)
                MyApplication.preferences.edit().putBoolean("isRegister", true).apply()
                startActivity(intent)
                finish()
            }, 1000)
        })

        viewModel.registerFail.observe(this, Observer {
            dismissProgress()
            showFail()
            Handler().postDelayed({
                dismissFail()
                dismissCheck()
                clearAndFocus()
                changeToFirstPassword()
            }, 1000)
        })
    }

    private fun showProgress() {
        register_ouze_password_progress.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        register_ouze_password_progress.visibility = View.GONE
    }

    private fun showCheck() {
        register_ouze_password_image_check.visibility = View.VISIBLE
    }

    private fun dismissCheck() {
        register_ouze_password_image_check.visibility = View.GONE
    }

    private fun showFail() {
        register_ouze_password_image_fail.visibility = View.VISIBLE
    }

    private fun dismissFail() {
        register_ouze_password_image_fail.visibility = View.GONE
    }

    private fun startChecking() {
        showProgress()
        Handler().postDelayed({
            dismissProgress()
            if (firstPassword.equals(secondPassword)) {
                showCheck()
                Handler().postDelayed({ verifyPassword() }, 1000)
            } else {
                showFail()
                Handler().postDelayed({
                    dismissFail()
                    changeToFirstPassword()
                }, 1000)
            }
        }, 1000)
    }

    private fun verifyPassword() {
        if (firstPassword.equals(secondPassword)) {
            checkRegistration()
        } else {
            Toast.makeText(this, "As senhas não estão equivalentes", Toast.LENGTH_LONG).show()
            clearAndFocus()
        }
    }

    private fun clearAndFocus() {
        binding.editTextPassword.text.clear()
    }

    private fun checkIfChangePasswordVisibility() {
        if (!checkBool) changeToSecondPassword()
        else startChecking()
    }

    private fun changeToSecondPassword() {
        clearAndFocus()
        changeToSecondText()
        checkBool = !checkBool
    }

    private fun changeToFirstPassword() {
        clearAndFocus()
        changeToFirstText()
        checkBool = !checkBool
    }

    private fun changeToSecondText() {
        register_ouze_password_text.text = getString(R.string.confirmar_sua_senha_text)
        button2.text = "Cadastrar"
    }

    private fun changeToFirstText() {
        button2.text = "Prosseguir"
        register_ouze_password_text.text = getString(R.string.crie_uma_senha_com_4_d_gitos)
    }

    private fun code(number: Int) {
        if (number == 1) firstPassword = editTextPassword.text.toString()
        else secondPassword = editTextPassword.text.toString()
    }

}
