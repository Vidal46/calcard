package br.com.calcard.android.app.ui.ouze

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.ui.login.LoginRedirectionActivity
import br.com.calcard.android.app.ui.redirection.ErrorHandlerActivity
import br.com.calcard.android.app.ui.redirection.RedirectionViewModel
import br.com.calcard.android.app.ui.redirection.RegistrationMiddleWareActivity
import br.com.calcard.android.app.ui.visa.CameraActivity
import br.com.calcard.android.app.ui.visa.ConfirmIdentityActivity
import br.com.calcard.android.app.ui.visa.OpsHandlerActivity
import br.com.calcard.android.app.ui.visa.VisaMigrationViewModel
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.CpfCnpjMaks
import br.com.calcard.android.app.utils.InputValidator
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_cpf_redirection.*

class CpfRedirectionFragment : BottomSheetDialogFragment() {

    private var elegibleCpf = MutableLiveData<MigrationElegible>()

    lateinit var input: TextInputLayout

    lateinit var inputText: EditText

    lateinit var progress: ProgressBar

    lateinit var ok: ImageView

    lateinit var fail: ImageView

    private var textBoolean = false

    private val migrationViewModel = VisaMigrationViewModel()

    private val viewModel: RedirectionViewModel by lazy {
        ViewModelProviders.of(this).get(RedirectionViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cpf_redirection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()

        setBinding()

        setObservers()
    }

    private fun bindViews() {
        input = ouze_bottom_sheet_main_redirection_input

        inputText = ouze_bottom_sheet_main_redirection_input_value

        progress = ouze_bottom_sheet_main_redirection_progress_ic

        ok = ouze_bottom_sheet_main_redirection_image_check

        fail = ouze_bottom_sheet_main_redirection_image_fail
    }

    private fun setBinding() {
        inputText.addTextChangedListener(CpfCnpjMaks.insert(input))

        inputText.addTextChangedListener(CodeDigitTextWatcher(R.layout.fragment_cpf_redirection, input))

        inputText.hint = "000.000.000-00"
        inputText.requestFocus()
    }

    private fun checkInputValue() {
        if (inputText.text?.trim()?.isNotEmpty()!! && inputText.text?.length == 14) {
            viewModel.checkSugestion(Constants.BEARER_API, inputText.text.toString())
        } else {
            showToast("Digite um CPF")
        }
    }

    private fun redirectToLogin() {
        setSharedPreferences("cpf", inputText.text.toString())
        val intent = Intent(activity, LoginRedirectionActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToRegistration() {
        val intent = Intent(activity, RegistrationMiddleWareActivity::class.java)
        intent.putExtra("cpf", inputText.text.toString())
        startActivity(intent)
    }

    private fun validateCpf(): Boolean {
        return InputValidator.isCPF(removePunctuation(inputText.text.toString()))
    }

    private fun removePunctuation(value: String?): String {
        return value?.replace("\\D".toRegex(), "") ?: ""
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        progress.visibility = View.GONE
    }

    private fun showCheck() {
        ok.visibility = View.VISIBLE
    }

    private fun animationOk() {
        checkInputValue()
    }

    private fun turnDownVisibilities() {
        dismissErrorBorder()
        ok.visibility = View.GONE
        fail.visibility = View.GONE
        progress.visibility = View.GONE
    }

    fun setSharedPreferences(key: String, user: String) {
        val sharedPreferences = activity!!.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, user)
        editor.apply()
    }

    private fun setObservers() {
        viewModel.sugestionCpf.observe(this, Observer {
            showCheck()
            when (it!!.action) {
                "PROSPECT" -> {
                    startActivity(Intent(activity, ErrorHandlerActivity::class.java).putExtra("codigo", "primeiro"))
                }
                "MIGRATION" -> {
                    redirectToMigration()
                }
                "REGISTRATION" -> {
                    redirectToRegistration()
                }
                "LOGIN" -> {
                    MyApplication.preferences.edit().putString("cpf", inputText.text.toString()).apply()
                    redirectToLogin()
                }
            }
            if (it.action != "MIGRATION") activity?.finish()
        })

        viewModel.handlerError.observe(this, Observer {
            showToast("Erro interno")
        })

        viewModel.serverError.observe(this, Observer {
            showToast("Erro de conexão")
        })
    }

    private fun onFailMigration(errorCode: String) {
        val intent = Intent(activity, OpsHandlerActivity::class.java)
        intent.putExtra("erro", errorCode)
        startActivity(intent)
    }

    private fun redirectToMigration() {
        val cpf = inputText.text.toString()

        migrationViewModel.getCpfEdible(Constants.BEARER_API, cpf)

        migrationViewModel.cpfEnable.observe(this, Observer {
            elegibleCpf.value = it
            val intent = Intent(activity, ConfirmIdentityActivity::class.java)
            intent.putExtra("migrationEdible", elegibleCpf.value)
            intent.putExtra("cpf", cpf)
            startActivity(intent)
        })

        migrationViewModel.connectionError.observe(this, Observer {
            Toast.makeText(activity, "Erro de conexão", Toast.LENGTH_LONG).show()
        })

        migrationViewModel.errorHandler.observe(this, Observer {
            when (it) {
                "1000" -> {
                    Toast.makeText(activity, "Favor aguardar dois minutos antes de pedir um novo código.", Toast.LENGTH_LONG).show()
                }
                "10000" -> {
                    Toast.makeText(activity, "Você já superou o máximo de tentativas de biometria, para a sua segurança, favor dirigir-se à loja mais próxima para efetuar a migração.", Toast.LENGTH_LONG).show()
                }
                "10001" -> {
                    Toast.makeText(activity, "Validação biométrica expirou, favor tirar nova foto.", Toast.LENGTH_LONG).show()
                }
                "10002" -> {
                    Toast.makeText(activity, "Nenhum telefone celular encontrado na base de dados, favor atualizar seu cadastro junto à Calcard pelo número 0800 653 033.", Toast.LENGTH_LONG).show()
                }
                "100000" -> {
                    Toast.makeText(activity, "Você já superou o máximo de envios de token, favor dirigir-se à loja mais próxima para efetuar a migração.", Toast.LENGTH_LONG).show()
                }
                "100001" -> {
                    Toast.makeText(activity, "Token inválido ou já expirado.", Toast.LENGTH_LONG).show()
                }
                else -> onFailMigration(it!!)
            }
        })
    }

    private fun showErrorBorder() {
        ouze_bottom_sheet_main_redirection_error_text.visibility = View.VISIBLE
        ouze_bottom_sheet_main_redirection_input.setBackgroundResource(R.drawable.error_border_alias)
    }

    private fun dismissErrorBorder() {
        ouze_bottom_sheet_main_redirection_error_text.visibility = View.GONE
        ouze_bottom_sheet_main_redirection_input.setBackgroundResource(R.drawable.brn_border_lilas)
    }

    companion object {

        @JvmStatic
        lateinit var mutableFlag: MutableLiveData<Boolean>
    }

    inner class CodeDigitTextWatcher(private val parent: Int,
                                     private val view: View
    ) : TextWatcher {
        override fun afterTextChanged(value: Editable?) {
            turnDownVisibilities()
            when {
                inputText.text?.length == 14 -> {
                    showProgress()
                    inputText.hint = ""
                    submit()
                }

                inputText.text?.length == 1 -> {
                    inputText.hint = ""
                }

                inputText.text?.isEmpty()!! -> {
                    inputText.hint = "000.000.000-00"
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        private fun submit() {
            Handler().postDelayed({
                if (validateCpf()) {
                    if (!textBoolean) {
                        textBoolean = true
                        Handler().postDelayed({
                            animationOk()
                        }, 1000)
                    }
                } else {
                    showErrorBorder()
                    dismissProgress()
                }
            }, 1000)
        }
    }

}
