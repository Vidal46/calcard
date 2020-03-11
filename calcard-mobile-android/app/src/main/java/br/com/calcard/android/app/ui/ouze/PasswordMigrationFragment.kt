package br.com.calcard.android.app.ui.ouze

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.ui.visa.PhoneNumberActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_password_migration.*
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class PasswordMigrationFragment : Fragment() {

    private var publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqfEwu2cxg8PUmOctzRbjMAFvhXwakxrWky1jAhmkRLmevGxc+b2kay/9/AiaRL3Exw6GL7SXo0Y2Hr1t6ODc6CwYOyVPeVPbx8za2inJSOxqT6GxOcrQuzhxOyF7jlbaAxQK+IEUsHhBKiE4Vd8J52RmJ1rzEYFiori8jbNUIOrM6WhloLdv0XGaVesToP9rD3kCbcRCWS7iXmHuisfMvHhTQLCGt2t0Cotu+1/+vGixDMXvDFbMrK0TWFF+3k+VZwSyBY5IKJdbBbj1ET/M7iocLOLXiCiIPLvRkue7v6ykKsMLNP23XJ/8n0+XXXzx5YjtGDgnpCcxG/PGXBxBawIDAQAB"

    private var checkBool = false

    private var password = String()

    private var firstPassword = String()

    private var secondPassword = String()

    var cpfIntent = String()

    var extraObj = MigrationElegible()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_password_migration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindCompanionValues()

        setBinding()
    }

    private fun bindCompanionValues() {
        extraObj = companionElegible.value!!

        cpfIntent = companionCpf.value!!
    }

    private fun setBinding() {
        first_ouze_password_input.editText?.addTextChangedListener(CodeDigitTextWatcher(this.requireView(), first_ouze_password_input))
        second_ouze_password_input.editText?.addTextChangedListener(CodeDigitTextWatcher(this.requireView(), second_ouze_password_input))
        third_ouze_password_input.editText?.addTextChangedListener(CodeDigitTextWatcher(this.requireView(), third_ouze_password_input))
        fourth_ouze_password_input.editText?.addTextChangedListener(CodeDigitTextWatcher(this.requireView(), fourth_ouze_password_input))
    }

    private fun changeToFirstText() {
        ouze_password_text.text = getString(R.string.crie_uma_senha_com_4_d_gitos)
    }

    private fun clearAndFocus() {
        first_ouze_password_input.editText!!.text.clear()
        second_ouze_password_input.editText!!.text.clear()
        third_ouze_password_input.editText!!.text.clear()
        fourth_ouze_password_input.editText!!.text.clear()
        first_ouze_password_cell.requestFocus()
    }

    private fun showProgress() {
        ouze_password_progress.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        ouze_password_progress.visibility = View.GONE
    }

    private fun showCheck() {
        ouze_password_image_check.visibility = View.VISIBLE
    }

    private fun dismissCheck() {
        ouze_password_image_check.visibility = View.GONE
    }

    private fun dismissFail() {
        ouze_password_image_check.visibility = View.GONE
    }

    private fun showFail() {
        ouze_password_image_fail.visibility = View.VISIBLE
    }

    private fun changeToFirstPassword() {
        clearAndFocus()
        changeToFirstText()
        checkBool = !checkBool
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

    fun encodeAndIntent(encryptedString: String) {
        val intent = Intent(activity, PhoneNumberActivity::class.java)
        intent.putExtra("encrypted", encryptedString)
        intent.putExtra("senha", password)
        intent.putExtra("migrationEdible", extraObj)
        intent.putExtra("cpf", cpfIntent)
        startActivity(intent)
    }

    private fun verifyPassword() {
        val one = encrypt(secondPassword)
        encodeAndIntent(one)
    }

    private fun changeToSecondText() {
        ouze_password_text.text = getString(R.string.confirmar_sua_senha_text)
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

    private fun changeToSecondPassword() {
        clearAndFocus()
        changeToSecondText()
        checkBool = !checkBool
    }

    private fun checkIfChangePasswordVisibility() {
        if (!checkBool) changeToSecondPassword()
        else startChecking()
    }

    private fun code(number: Int) {
        val um = first_ouze_password_input.editText?.text
        val dois = second_ouze_password_input.editText?.text
        val tres = third_ouze_password_input.editText?.text
        val quatro = fourth_ouze_password_input.editText?.text

        if (number == 1) firstPassword = "$um$dois$tres$quatro"
        else secondPassword = "$um$dois$tres$quatro"
    }

    companion object {

        @JvmStatic
        var companionElegible = MutableLiveData<MigrationElegible>()

        @JvmStatic
        var companionCpf = MutableLiveData<String>()
    }

    inner class CodeDigitTextWatcher(private val parent: View,
                                     private val view: View
    ) : TextWatcher {
        override fun afterTextChanged(value: Editable?) {
            if (value!!.isEmpty()) {
            } else {
                when (view.id) {
                    R.id.first_ouze_password_input -> {
                        parent.findViewById<TextInputLayout>(R.id.second_ouze_password_input).requestFocus()
                    }

                    R.id.second_ouze_password_input -> {
                        parent.findViewById<TextInputLayout>(R.id.third_ouze_password_input).requestFocus()
                    }

                    R.id.third_ouze_password_input -> {
                        parent.findViewById<TextInputLayout>(R.id.fourth_ouze_password_input).requestFocus()
                    }

                    else -> {
                        if (!checkBool) code(1)
                        else code(2)
                        checkIfChangePasswordVisibility()
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }
}
