package br.com.calcard.android.app.ui.smsverification

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProviders
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.ActivitySmsConfirmationBinding
import br.com.calcard.android.app.ui.register.OuzeRegisterPasswordActivity
import br.com.calcard.android.app.utils.Constants
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_sms_confirmation.*

class OuzeSmsConfirmationActivity : AppCompatActivity() {

    internal lateinit var binding: ActivitySmsConfirmationBinding

    internal lateinit var code: String

    internal lateinit var cpf: String

    internal lateinit var alert: AlertDialog

    private var hasPhone = false

    lateinit var phone: String

    private val activityContext = this@OuzeSmsConfirmationActivity

    val viewModel: SmsConfirmationViewModel by lazy {
        ViewModelProviders.of(activityContext).get(SmsConfirmationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_confirmation)
        binding = DataBindingUtil.setContentView(activityContext, R.layout.activity_sms_confirmation)
        binding.executePendingBindings()

        intent.run {
            cpf = getStringExtra("cpf")
            if (getStringExtra("phone") != null) {
                phone = getStringExtra("phone")
                hasPhone = true
            }
        }

        oze_sms_register_link.setOnClickListener {
            onClickResend()
        }

        bindTextWatcher()
        if (hasPhone) setPhoneText()
        binds()
    }

    private fun bindTextWatcher() {
        binding.firstOzeSmsRegisterInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.firstOzeSmsRegisterInput))
        binding.secondOzeSmsRegisterInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.secondOzeSmsRegisterInput))
        binding.thirdOzeSmsRegisterInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.thirdOzeSmsRegisterInput))
        binding.fourthOzeSmsRegisterInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.fourthOzeSmsRegisterInput))
        binding.fifthOzeSmsRegisterInput.editText?.addTextChangedListener(CodeDigitTextWatcher(binding.root, binding.fifthOzeSmsRegisterInput))
    }

    private fun setPhoneText() {
        oze_sms_register_text.text = "Digite o código enviado para $phone"
    }

    private fun showProgress() {
        oze_sms_register_progress.visibility = View.VISIBLE
    }

    private fun showSuccess() {
        oze_sms_register_check.visibility = View.VISIBLE
    }

    private fun showFail() {
        oze_sms_register_fail.visibility = View.VISIBLE
    }

    private fun dismissViews() {
        oze_sms_register_check.visibility = View.GONE
        oze_sms_register_fail.visibility = View.GONE
        oze_sms_register_progress.visibility = View.GONE
    }

    private fun setObserver() {
        viewModel.errorCodeLiveDataBoolean.observe(this, androidx.lifecycle.Observer {
            showFail()
            Handler().postDelayed({ dismissViews(); clearCode() }, 1000)
        })

        viewModel.successCodeLiveData.observe(this, androidx.lifecycle.Observer {
            showSuccess()
            Handler().postDelayed({
                dismissViews()

            }, 1000)
        })
    }

    fun binds() {

        viewModel.cpfOk.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                val cpfOk = viewModel.cpfOk.get()
                if (cpfOk == "valido") {
                    openDialog("Novo código de ativação enviado para o número: " + intent.getStringExtra("phone"))
                } else if (cpfOk != "") {
                    if (viewModel.phone.get() == null) {
                        openDialog(cpfOk!!)
                    }
                }
                viewModel.cpfOk.set("")
            }
        })

        viewModel.tokenOk.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (viewModel.tokenOk.get().equals("ok")) {
                    dismissViews()
                    showSuccess()
                    Handler().postDelayed({
                        val intent = Intent(this@OuzeSmsConfirmationActivity, OuzeRegisterPasswordActivity::class.java)
                        intent.putExtra("cpf", cpf)
                        val forgot = getIntent().getBooleanExtra("isForgot", false)
                        intent.putExtra("isForgot", forgot)
                        intent.putExtra("tokenSMS", code)
                        startActivity(intent)
                        finish()
                    }, 1000)
                } else if (viewModel.tokenOk.get().equals("n")) {
                    showFail()
                    Handler().postDelayed({ dismissViews(); clearCode() }, 1000)
                }
                viewModel.tokenOk.set("")

            }
        })
    }

    private fun clearCode() {
        binding.firstOzeSmsRegisterInput.editText?.text?.clear()
        binding.secondOzeSmsRegisterInput.editText?.text?.clear()
        binding.thirdOzeSmsRegisterInput.editText?.text?.clear()
        binding.fourthOzeSmsRegisterInput.editText?.text?.clear()
        binding.fifthOzeSmsRegisterInput.editText?.text?.clear()
        binding.firstOzeSmsRegisterInput.editText?.requestFocus()
    }

    private fun code() {
        val um = binding.firstOzeSmsRegisterInput.editText?.text
        val dois = binding.secondOzeSmsRegisterInput.editText?.text
        val tres = binding.thirdOzeSmsRegisterInput.editText?.text
        val quatro = binding.fourthOzeSmsRegisterInput.editText?.text
        val cinco = binding.fifthOzeSmsRegisterInput.editText?.text

        code = "$um$dois$tres$quatro$cinco"
    }

    fun sendCode() {
        viewModel.validateToken(Constants.BEARER_API, cpf.replace(".", "").replace("-", ""), code)
    }

    fun openDialog(text: String) {
        val li = layoutInflater
        val v = li.inflate(R.layout.modal_resend_code, null)
        val builder = AlertDialog.Builder(this@OuzeSmsConfirmationActivity)
        builder.setView(v)
        val tv = v.findViewById<TextView>(R.id.tvDescribe)
        tv.text = text
        val t = v.findViewById<ImageView>(R.id.imgClose)
        t.setOnClickListener { v1 -> alert.dismiss() }
        alert = builder.create()
        alert.show()
    }

    fun openProblemsCode(view: View) {
        val li = layoutInflater
        val v = li.inflate(R.layout.modal_problems_code, null)
        val builder = AlertDialog.Builder(this@OuzeSmsConfirmationActivity)
        builder.setView(v)
        val btn = v.findViewById<Button>(R.id.button13)
        btn.setOnClickListener { vv ->
            alert.dismiss()
            val li1 = layoutInflater
            val vvv = li1.inflate(R.layout.modal_suport, null)
            val builder1 = AlertDialog.Builder(this@OuzeSmsConfirmationActivity)
            builder1.setView(vvv)
            val t = vvv.findViewById<ImageView>(R.id.imageView14)
            t.setOnClickListener { v1 -> alert.dismiss() }
            alert = builder1.create()
            alert.show()
        }
        val t = v.findViewById<ImageView>(R.id.imageView13)
        t.setOnClickListener { v1 -> alert.dismiss() }
        alert = builder.create()
        alert.show()
    }

    fun onClickResend() {
        viewModel.generateToken(Constants.BEARER_API, cpf)
    }

    fun onClickConfirm(view: View) {
        viewModel.validateToken(Constants.BEARER_API, cpf.replace(".", "").replace("-", ""), code)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submitOnBlur() {
        showProgress()
        Handler().postDelayed({
            code()
            sendCode()
        }, 1000)
    }

    inner class CodeDigitTextWatcher(private val parent: View,
                                     private val view: View
    ) : TextWatcher {
        override fun afterTextChanged(value: Editable?) {
            if (value!!.isEmpty()) {
            } else {
                when (view.id) {
                    R.id.first_oze_sms_register_input -> {
                        parent.findViewById<TextInputLayout>(R.id.second_oze_sms_register_input).requestFocus()
                    }

                    R.id.second_oze_sms_register_input -> {
                        parent.findViewById<TextInputLayout>(R.id.third_oze_sms_register_input).requestFocus()
                    }

                    R.id.third_oze_sms_register_input -> {
                        parent.findViewById<TextInputLayout>(R.id.fourth_oze_sms_register_input).requestFocus()
                    }

                    R.id.fourth_oze_sms_register_input -> {
                        parent.findViewById<TextInputLayout>(R.id.fifth_oze_sms_register_input).requestFocus()
                    }

                    else -> {
                        if (validateInputs()) {
                            if (validateInputs()) {
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

            if (binding.firstOzeSmsRegisterInput.editText?.text?.isEmpty()!!) {
                valid = false
            }

            if (binding.secondOzeSmsRegisterInput.editText?.text?.isEmpty()!!) {
                valid = false
            }

            if (binding.thirdOzeSmsRegisterInput.editText?.text?.isEmpty()!!) {
                valid = false
            }

            if (binding.fourthOzeSmsRegisterInput.editText?.text?.isEmpty()!!) {
                valid = false
            }

            if (binding.fifthOzeSmsRegisterInput.editText?.text?.isEmpty()!!) {
                valid = false
            }
            return valid
        }
    }

}
