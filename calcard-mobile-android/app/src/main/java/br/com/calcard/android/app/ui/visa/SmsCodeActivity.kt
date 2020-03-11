package br.com.calcard.android.app.ui.visa

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.ActivitySmsCodeOuzeBinding
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.utils.Constants
import com.google.android.material.textfield.TextInputLayout
//import kotlinx.android.synthetic.main.activity_sms_code.*
//import kotlinx.android.synthetic.main.activity_sms_code.fifth_visa_sms_code_input
//import kotlinx.android.synthetic.main.activity_sms_code.first_visa_sms_code_input
//import kotlinx.android.synthetic.main.activity_sms_code.fourth_visa_sms_code_input
//import kotlinx.android.synthetic.main.activity_sms_code.second_visa_sms_code_input
//import kotlinx.android.synthetic.main.activity_sms_code.third_visa_sms_code_input
import kotlinx.android.synthetic.main.activity_sms_code_ouze.*

class SmsCodeActivity : AppCompatActivity() {
    lateinit var password: String
    lateinit var rsa: String
    lateinit var countDown: CountDownTimer
    lateinit var alert: AlertDialog

    val viewModel = VisaMigrationViewModel()
    var cpfIntent = String()
    var deviceId = String()
    var extraObj = MigrationElegible()
    var codeSms = String()
    var timeRunning = true
    var timeMilliSecond: Long = 120000

    lateinit var binding: ActivitySmsCodeOuzeBinding

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sms_code_ouze)

        intent.run {
            password = getStringExtra("senha")
            rsa = getStringExtra("encrypted")
            cpfIntent = getStringExtra("cpf")
            extraObj = getSerializableExtra("migrationEdible") as MigrationElegible
        }

        deviceId = Settings.Secure.getString(this.contentResolver,
                Settings.Secure.ANDROID_ID)

        bindViews()
        setObservers()
    }

    private fun showProgress() {
        sms_code_progress.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        sms_code_progress.visibility = View.GONE
    }

    private fun showCheck() {
        sms_code_image_check.visibility = View.VISIBLE
    }

    private fun showFail() {
        sms_code_image_fail.visibility = View.VISIBLE
    }

    private fun turnVisibilityDown() {
        sms_code_image_fail.visibility = View.GONE
        sms_code_image_check.visibility = View.GONE
        sms_code_progress.visibility = View.GONE
    }

    private fun setObservers() {
        viewModel.codeOk.observe(this, Observer {
            if (viewModel.codeOk.value == true) {
                showCheck()
                Handler().postDelayed({
                    dismissProgress()
                    okStartActivity()
                }, 1000)
            }
        })

        viewModel.connectionError.observe(this, Observer {
            Toast.makeText(this, "Erro de conexão", Toast.LENGTH_SHORT).show()
        })

        viewModel.errorHandler.observe(this, Observer {
            when (it) {
                "1000" -> {
                    showToast("Favor aguardar dois minutos antes de pedir um novo código.")
                }
                "10000" -> {
                    showToast("Você já superou o máximo de tentativas de biometria, para a sua segurança, favor dirigir-se à loja mais próxima para efetuar a migração.")
                }
                "10001" -> {
                    showToast("Validação biométrica expirou, favor tirar nova foto")
                }
                "100000" -> {
                    showToast("Você já superou o máximo de envios de token, favor dirigir-se à loja mais próxima para efetuar a migração.")
                }
                "100001" -> {
                    showToast("Token inválido ou já expirado.")
                }
                else -> showToast(it!!)
            }
            showFail()
            Handler().postDelayed({
                turnVisibilityDown()
                dismissProgress()
                clearCode()
            }, 1000)
        })
    }


//    private fun setClickListener() {
//        visa_sms_code_text.setOnClickListener {
//            startTimer()
//            viewModel.getCpfEdible(Constants.BEARER_API, cpfIntent)
//            visa_sms_code_text.visibility = View.GONE
//            visa_sms_code_timer.visibility = View.VISIBLE
//        }
//
//        binding.smsCodeLink.setOnClickListener {
//            startTimer()
//            viewModel.getCpfEdible(Constants.BEARER_API, cpfIntent)
////            visa_sms_code_text.visibility = View.GONE
////            visa_sms_code_timer.visibility = View.VISIBLE
//        }
//
//        visa_sms_code_problem.setOnClickListener {
//            openProblemsCode()
//        }
//    }

    private fun bindViews() {

        binding.firstVisaSmsCodeInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.firstVisaSmsCodeInput))

        binding.secondVisaSmsCodeInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.secondVisaSmsCodeInput))

        binding.thirdVisaSmsCodeInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.thirdVisaSmsCodeInput))

        binding.fourthVisaSmsCodeInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.fourthVisaSmsCodeInput))

        binding.fifthVisaSmsCodeInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.fifthVisaSmsCodeInput))
    }

    private fun submitOnBlur() {
        if (first_visa_sms_code_input?.editText?.text?.isNotEmpty()!!
                && second_visa_sms_code_input?.editText?.text?.isNotEmpty()!!
                && third_visa_sms_code_input?.editText?.text?.isNotEmpty()!!
                && fourth_visa_sms_code_input?.editText?.text?.isNotEmpty()!!
                && fifth_visa_sms_code_input?.editText?.text?.isNotEmpty()!!
        ) {
            val codigo = code()
            showProgress()
            Handler().postDelayed({ viewModel.checkCodePassword(Constants.BEARER_API, rsa, codigo, cpfIntent, deviceId) }, 1000)
        } else {
            showToast("Favor preencher todos os campos")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    private fun startTimer() {
//        countDown = object : CountDownTimer(timeMilliSecond, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                timeMilliSecond = millisUntilFinished
//                updateTimer()
//            }
//
//            override fun onFinish() {
//                visa_sms_code_text.visibility = View.VISIBLE
//                visa_sms_code_timer.visibility = View.INVISIBLE
//                timeMilliSecond = 120000
//            }
//
//        }.start()
//    }

//    private fun updateTimer() {
//        val min = timeMilliSecond.toInt() / 60000
//        val sec = timeMilliSecond.toInt() % 60000 / 1000
//
//        var textTimeLeft: String
//        textTimeLeft = "$min"
//        textTimeLeft += ":"
//        if (sec < 10) {
//            textTimeLeft += "0"
//        }
//        textTimeLeft += sec
//        visa_sms_code_timer.setText(textTimeLeft)
//    }

    private fun openProblemsCode() {
        val layoutInflater = layoutInflater
        val inflater = layoutInflater.inflate(R.layout.modal_problems_code, null)
        val builder = AlertDialog.Builder(this@SmsCodeActivity)
        builder.setView(inflater)
        val btn = inflater.findViewById<Button>(R.id.button13)
        btn.setOnClickListener { input ->
            alert.dismiss()
            val insidelayoutInflater = layoutInflater
            val insideInflater = insidelayoutInflater.inflate(R.layout.modal_suport, null)
            val builder1 = AlertDialog.Builder(this@SmsCodeActivity)
            builder1.setView(insideInflater)
            val t = insideInflater.findViewById<ImageView>(R.id.imageView14)
            t.setOnClickListener { v1 -> alert.dismiss() }
            alert = builder1.create()
            alert.show()
        }
        val t = inflater.findViewById<ImageView>(R.id.imageView13)
        t.setOnClickListener { v1 -> alert.dismiss() }
        alert = builder.create()
        alert.show()
    }

    private fun code(): String {
        val um = binding.firstVisaSmsCodeInput.editText?.text
        val dois = binding.secondVisaSmsCodeInput.editText?.text
        val tres = binding.thirdVisaSmsCodeInput.editText?.text
        val quatro = binding.fourthVisaSmsCodeInput.editText?.text
        val cinco = binding.fifthVisaSmsCodeInput.editText?.text

        return "$um$dois$tres$quatro$cinco"
    }

    private fun clearCode() {
        binding.firstVisaSmsCodeInput.editText?.text?.clear()
        binding.secondVisaSmsCodeInput.editText?.text?.clear()
        binding.thirdVisaSmsCodeInput.editText?.text?.clear()
        binding.fourthVisaSmsCodeInput.editText?.text?.clear()
        binding.fifthVisaSmsCodeInput.editText?.text?.clear()
    }

    private fun okStartActivity() {
        intent = Intent(this, WellVerifyMigrationActivity::class.java)
        startActivity(intent)
    }

    inner class CodeDigitTextWatcher(private val parent: View,
                                     private val view: View
    ) : TextWatcher {
        override fun afterTextChanged(value: Editable?) {
            if (value!!.isEmpty()) {
            } else {
                when (view.id) {
                    R.id.first_visa_sms_code_input -> {
                        if (validateInputs()) {
                            codeSms.plus(binding.firstVisaSmsCodeCell.text)
                            parent.findViewById<TextInputLayout>(R.id.second_visa_sms_code_input).requestFocus()
                        } else {
                            parent.findViewById<TextInputLayout>(R.id.second_visa_sms_code_input).requestFocus()
                        }
                    }

                    R.id.second_visa_sms_code_input -> {
                        if (validateInputs()) {
                            codeSms.plus(binding.secondVisaSmsCodeCell.text)
                            parent.findViewById<TextInputLayout>(R.id.third_visa_sms_code_input).requestFocus()
                        } else {
                            parent.findViewById<TextInputLayout>(R.id.third_visa_sms_code_input).requestFocus()
                        }
                    }

                    R.id.third_visa_sms_code_input -> {
                        if (validateInputs()) {
                            codeSms.plus(binding.thirdVisaSmsCodeCell.text)
                            parent.findViewById<TextInputLayout>(R.id.fourth_visa_sms_code_input).requestFocus()
                        } else {
                            parent.findViewById<TextInputLayout>(R.id.fourth_visa_sms_code_input).requestFocus()
                        }
                    }

                    R.id.fourth_visa_sms_code_input -> {
                        if (validateInputs()) {
                            codeSms.plus(binding.fourthVisaSmsCodeCell.text)
                            parent.findViewById<TextInputLayout>(R.id.fifth_visa_sms_code_input).requestFocus()
                        } else {
                            parent.findViewById<TextInputLayout>(R.id.fifth_visa_sms_code_input).requestFocus()
                        }
                    }
                    else -> {
                        if (validateInputs()) {
                            if (validateInputs()) {
                                codeSms.plus(binding.fifthVisaSmsCodeInput)
                                submitOnBlur()
                            }
                        }
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        fun validateInputs(): Boolean {
            var valid = true

            if (binding.firstVisaSmsCodeInput.editText?.text?.isEmpty()!!) {
                valid = false
            }

            if (binding.secondVisaSmsCodeInput.editText?.text?.isEmpty()!!) {
                valid = false
            }

            if (binding.thirdVisaSmsCodeInput.editText?.text?.isEmpty()!!) {
                valid = false
            }

            if (binding.fourthVisaSmsCodeInput.editText?.text?.isEmpty()!!) {
                valid = false
            }

            if (binding.fifthVisaSmsCodeInput.editText?.text?.isEmpty()!!) {
                valid = false
            }
            return valid
        }
    }

}
