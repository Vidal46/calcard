package br.com.calcard.android.app.ui.ouze

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.cards.ChangePasswordViewModel
import br.com.calcard.android.app.ui.changepasswordcard.ChangePasswordCardViewModel
import br.com.calcard.android.app.ui.ouze.HomePasswordFragment.CardFlow.APP
import br.com.calcard.android.app.ui.ouze.HomePasswordFragment.CardFlow.CARD
import br.com.calcard.android.app.ui.register.InputDataViewModel
import br.com.calcard.android.app.ui.register.RegisterViewModel
import br.com.calcard.android.app.ui.smsverification.SmsConfirmationViewModel
import br.com.calcard.android.app.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_block_card1fragment.*
import kotlinx.android.synthetic.main.fragment_home_password.*


class HomePasswordFragment : BottomSheetDialogFragment() {

    private val fragmentContext = this@HomePasswordFragment

    private lateinit var bottomSheetFragment: BottomSheetDialogFragment

    private var cvv = String()

    private var token = String()

    private var oldPassword = String()

    private var newPassword = String()

    private var newPasswordConfirmation = String()

    private lateinit var typeFlow: CardFlow

    private lateinit var typeLabel: String

    lateinit var typeChange: TypeChange

    private var countStepCard = 0

    private var countStepApp = 0

    private var phoneNumber = String()

    private val cardPasswordViewModel by lazy {
        ViewModelProviders.of(fragmentContext).get(ChangePasswordCardViewModel::class.java)
    }

    private val appPasswordViewModel by lazy {
        ViewModelProviders.of(fragmentContext).get(ChangePasswordViewModel::class.java)
    }

    private val inputDataViewModel by lazy {
        ViewModelProviders.of(fragmentContext).get(InputDataViewModel::class.java)
    }

    private val registerViewModel by lazy {
        ViewModelProviders.of(fragmentContext).get(RegisterViewModel::class.java)
    }

    private val smsConfirmationViewModel by lazy {
        ViewModelProviders.of(fragmentContext).get(SmsConfirmationViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_password, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        setBottomSheet()

        setClickListeners()

        setTextWatcher()
    }

    private fun setTextWatcher() {
        home_password_third_apppassword_input_value_forgot.addTextChangedListener(CodeDigitTextWatcher(R.layout.fragment_home_password, home_password_third_apppassword_input_value_forgot))
    }

    private fun setBottomSheet() {
        val actions = arrayListOf<BottomSheetActions>(BottomSheetActions.CATAO_CVV)
        bottomSheetFragment = CustomBottomSheetDialogFragment.newInstance(actions)
        bottomSheetFragment.run {
            isCancelable = false
        }
    }

    private fun setObservers() {

        appPasswordViewModel.run {
            success.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                changeFinalView()
            })

            error.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                showToast(it)
            })

            fail.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                showToast("Erro de conexão")
            })
        }

        cardPasswordViewModel.run {
            success.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                changeFinalView()
            })

            error.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                showToast(it)
            })

            fail.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                showToast("Erro de conexão")
            })
        }

        registerViewModel.run {
            cpfObserver.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                changeForgotAppPasswordView()
            })

            mutableLivePhone.observe(fragmentContext, Observer {
                phoneNumber = mutableLivePhone.value!!
                setPhoneText()
            })

            errorHandler.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                showToast(it)
            })

            fail.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                showToast("Erro de conexão")
            })
        }

        smsConfirmationViewModel.run {
            successCodeLiveData.observe(fragmentContext, Observer {

                home_password_third_progress.visibility = View.GONE
                typeChange = TypeChange.UPDATE
                countStepApp = 0
                changeSecondView()
            })

            errorCodeLiveDataString.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                showToast(it)
            })

            failCodeLiveData.observe(fragmentContext, Observer {
                home_password_third_progress.visibility = View.GONE
                showToast("Erro de conexão")
            })
        }
    }

    private fun setPhoneText() {
        home_password_third_app_title_forgot.text = phoneNumber
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun setFlow() {
        typeLabel = when (typeFlow) {
            CARD -> "cartão"

            APP -> "aplicativo"
        }
    }

    private fun setClickListeners() {
        home_password_first_card_button.setOnClickListener {
            changeFirstView()
            typeFlow = CARD
            setFlow()
            chooseSecondFlow()
        }

        home_password_third_link.setOnClickListener {
            bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
        }

        home_password_first_app_button.setOnClickListener {
            changeFirstView()
            typeFlow = APP
            setFlow()
            chooseSecondFlow()
        }

        //TODO
        home_password_second_up_button.setOnClickListener {
            typeChange = TypeChange.FORGOT
            changeSecondView()
            chooseThirdFlow()
        }

        home_password_second_down_button.setOnClickListener {
            typeChange = TypeChange.UPDATE
            changeSecondView()
            chooseThirdFlow()
        }

        home_password_third_cancelar_button.setOnClickListener {
            mutableFlag.value = true
        }

        home_password_fourth_button_submit.setOnClickListener {
            mutableFlag.value = true
        }

        home_password_third_button_submit.setOnClickListener {
            buttonMutableOptionsCard()
        }

        home_password_third_app_cancelar_button.setOnClickListener {
            mutableFlag.value = true
        }

        home_password_third_app_button_submit.setOnClickListener {
            buttonMutableOptionsApp()
        }

        home_password_third_app_cancelar_button_forgot.setOnClickListener {
            mutableFlag.value = true
        }
    }

    private fun buttonMutableOptionsApp() {
        when (countStepApp) {
            0 -> {
                home_password_third_app_title.text = "Digite sua nova senha"
                newPassword = home_password_third_apppassword_input_value.text.toString()
                clearAndFocus()
            }

            1 -> {
                home_password_third_app_title.text = "Confirme sua nova senha"
                newPasswordConfirmation = home_password_third_apppassword_input_value.text.toString()
                clearAndFocus()
            }

            2 -> {
                changePassword()
            }
        }
    }

    private fun buttonMutableOptionsCard() {
        when (countStepCard) {
            0 -> {
                home_password_third_title.text = "Insira a senha atual do cartão"
                home_password_third_link.text = "Esqueci a senha do cartão"
                cvv = home_password_third_input_value.text.toString()
                home_password_third_input_value.limitLength(4)
                clearAndFocus()
            }

            1 -> {
                home_password_third_title.text = "Senha atual do $typeLabel"
                oldPassword = home_password_third_input_value.text.toString()
                clearAndFocus()
            }

            2 -> {
                home_password_third_title.text = "Digite sua nova senha"
                home_password_third_link.visibility = View.GONE
                newPassword = home_password_third_input_value.text.toString()
                clearAndFocus()
            }

            3 -> {
                home_password_third_title.text = "Confirme sua nova senha"
                newPasswordConfirmation = home_password_third_input_value.text.toString()
                clearAndFocus()
            }

            4 -> {
                changePassword()

            }
        }
    }

    private fun clearAndFocus() {
        home_password_third_input_value.text.clear()
        home_password_third_apppassword_input_value.text.clear()

        when (typeFlow) {
            CARD -> {
                home_password_third_input_value.text.clear()
            }

            APP -> {
                home_password_third_apppassword_input_value.clearFocus()
            }
        }

        countStepCard += 1
        countStepApp += 1
    }

    private fun chooseSecondFlow() {

        if (typeFlow.equals(CARD)) {
            home_password_second_text.visibility = View.INVISIBLE

            home_password_second_up_button.visibility = View.INVISIBLE
        } else {
            home_password_second_up_button.text = "Esqueci a senha do $typeLabel"

            home_password_third_apppassword_input.visibility = View.VISIBLE
        }

        home_password_second_title.text = "Senha do $typeLabel"

        home_password_second_title.text = "Senha do $typeLabel"

        home_password_second_down_button.text = "alterar a senha do $typeLabel"
    }

    private fun chooseThirdFlow() {

        home_password_third_text.text = "Senha do $typeLabel"
    }

    private fun changeFirstView() {
        home_password_first_constraint.visibility = View.GONE
        home_password_second_constraint.visibility = View.VISIBLE
    }

    private fun changeSecondView() {
        home_password_third_app_constraint_forgot.visibility = View.GONE
        home_password_second_constraint.visibility = View.GONE
        when {

            typeFlow == APP && typeChange == TypeChange.FORGOT -> {
                registerViewModel.generateToken(Constants.BEARER_API, MyApplication.preferences.getString("cpf", null), "FORGOT_PASSWORD")
                home_password_third_progress.visibility = View.VISIBLE
            }

            typeFlow == APP && typeChange == TypeChange.UPDATE -> {
                home_password_third_app_title.text = "Digite sua nova senha"
                home_password_third_app_constraint.visibility = View.VISIBLE

            }

            typeFlow == CARD -> home_password_third_constraint.visibility = View.VISIBLE
        }
    }

    private fun changeForgotAppPasswordView() {
        home_password_third_progress.visibility = View.GONE
        home_password_second_constraint.visibility = View.GONE
        home_password_third_app_constraint_forgot.visibility = View.VISIBLE
    }

    private fun changeFinalView() {
        if (typeFlow == APP) {
            home_password_third_input.visibility = View.INVISIBLE
        }

        home_password_third_app_constraint.visibility = View.GONE
        home_password_third_constraint.visibility = View.GONE
        home_password_fourth_constraint.visibility = View.VISIBLE
        home_password_fourth_title.text = "Senha do $typeLabel"
    }

    private fun EditText.limitLength(maxLength: Int) {
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

    private fun changePassword() {
        home_password_third_progress.visibility = View.VISIBLE
        when (typeFlow) {

            CARD -> cardPasswordViewModel.changePassword(newPassword, oldPassword, cvv)

            APP -> appPasswordViewModel.changePassword(oldPassword, newPasswordConfirmation)
        }
    }

    private fun forgotPassword() {
        @SuppressLint("HardwareIds") val android_id = Settings.Secure.getString(fragmentContext.activity!!.contentResolver,
                Settings.Secure.ANDROID_ID)

        when (typeFlow) {
            CARD -> {
                showToast("Não é possível realizar a recuperação de senha pelo acplicativo. Vá até uma loja Studio Z")
            }
            APP -> inputDataViewModel.forgot(android_id, MyApplication.preferences.getString("cpf", ""), token, newPassword)
        }
    }

    private enum class CardFlow {
        CARD,
        APP
    }

    enum class TypeChange {
        UPDATE,
        FORGOT
    }

    companion object {
        @JvmStatic
        lateinit var mutableFlag: MutableLiveData<Boolean>
    }

    private fun validateToken() {
        home_password_third_progress.visibility = View.VISIBLE
        smsConfirmationViewModel.validateToken(
                Constants.BEARER_API,
                MyApplication.preferences.getString("cpf", ""),
                home_password_third_apppassword_input_value_forgot.text.toString()
        )
    }

    inner class CodeDigitTextWatcher(private val parent: Int,
                                     private val view: View
    ) : TextWatcher {
        override fun afterTextChanged(value: Editable?) {
            when {
                home_password_third_apppassword_input_value_forgot.text!!.length < 5 -> {
                    home_password_third_app_button_submit_forgot.setBackgroundResource(R.drawable.btn_disable_square)
                    home_password_third_app_button_submit_forgot.setOnClickListener { _ ->
                        {}
                    }
                }

                home_password_third_apppassword_input_value_forgot.text!!.length >= 5 -> {
                    home_password_third_app_button_submit_forgot.setBackgroundResource(R.drawable.btn_orange)
                    home_password_third_app_button_submit_forgot.setOnClickListener {
                        validateToken()
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
