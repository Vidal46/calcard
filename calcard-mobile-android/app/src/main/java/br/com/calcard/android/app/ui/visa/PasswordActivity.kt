package br.com.calcard.android.app.ui.visa

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.ActivityPasswordBinding
import br.com.calcard.android.app.model.MigrationElegible
import kotlinx.android.synthetic.main.activity_password.*
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


class PasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityPasswordBinding
    private var publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqfEwu2cxg8PUmOctzRbjMAFvhXwakxrWky1jAhmkRLmevGxc+b2kay/9/AiaRL3Exw6GL7SXo0Y2Hr1t6ODc6CwYOyVPeVPbx8za2inJSOxqT6GxOcrQuzhxOyF7jlbaAxQK+IEUsHhBKiE4Vd8J52RmJ1rzEYFiori8jbNUIOrM6WhloLdv0XGaVesToP9rD3kCbcRCWS7iXmHuisfMvHhTQLCGt2t0Cotu+1/+vGixDMXvDFbMrK0TWFF+3k+VZwSyBY5IKJdbBbj1ET/M7iocLOLXiCiIPLvRkue7v6ykKsMLNP23XJ/8n0+XXXzx5YjtGDgnpCcxG/PGXBxBawIDAQAB"
    private var privateKey = String()
    private var encodeData: ByteArray? = null
    var cpfIntent = String()
    var extraObj = MigrationElegible()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password)

        intent.run {
            extraObj = getSerializableExtra("migrationEdible") as MigrationElegible
            cpfIntent = getStringExtra("cpf")
        }

        binding.visaPasswordContinueButton.setOnClickListener {
            if (!binding.visaPasswordFirstValue.text.trim().isEmpty() && !binding.visaPasswordSecondValue.text.trim().isEmpty()) {
                verifyPassword()
            } else {
                Toast.makeText(this, "Todos os campos devem estar preenchidos", Toast.LENGTH_LONG).show()
                clearAndFocus()
            }
        }
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

    private fun verifyPassword() {
        visa_password_progress.visibility = View.VISIBLE
        var firstInput = binding.visaPasswordFirstValue.text.toString()
        var secondInput = binding.visaPasswordSecondValue.text.toString()

        if (firstInput.equals(secondInput)) {
            val one = encrypt(secondInput)
            encodeAndIntent(one)
        } else {
            Toast.makeText(this, "As senhas não estão equivalentes", Toast.LENGTH_LONG).show()
            visa_password_progress.visibility = View.GONE
            clearAndFocus()
        }
    }

    fun encodeAndIntent(encryptedString: String) {
        intent = Intent(this, SmsCodeActivity::class.java)
        intent.putExtra("encrypted", encryptedString)
        intent.putExtra("senha", binding.visaPasswordFirstValue.text.toString())
        intent.putExtra("migrationEdible", extraObj)
        intent.putExtra("cpf", cpfIntent)
        visa_password_progress.visibility = View.GONE
        startActivity(intent)
    }

    private fun clearAndFocus() {
        binding.visaPasswordFirstValue.text.clear()
        binding.visaPasswordSecondValue.text.clear()
        binding.visaPasswordFirstValue.requestFocus()
    }

}
