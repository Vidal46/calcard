package br.com.calcard.android.app.ui.ouze

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.loss_theft.ContinueLossTheftViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_block_card1fragment.*

class BlockCardFragment : Fragment() {

    lateinit var bottomSheet: BottomSheetDialogFragment
    private lateinit var inputText: EditText
    var title: String? = null
    var reason: String? = null
    var vModel = ContinueLossTheftViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_block_card1fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputText = block_fragment_input_value
        setClickListener()
        setTextwatcher()
    }

    fun setObservers() {
        vModel.responseMessage.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (vModel.responseMessage.get().equals("ok")) {
                    block_fragment_senha_container.visibility = View.GONE
                    fragment_container_sucesso.visibility = View.VISIBLE

                } else {
                    Toast.makeText(activity, vModel.responseMessage.get(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun setTextwatcher() {
        inputText.addTextChangedListener(CodeDigitTextWatcher(R.layout.fragment_block_card1fragment, block_fragment_input_value))
    }

    fun setClickListener() {

        block_fragment_fui_roubado.setOnClickListener {
            block_motivo_container.visibility = View.GONE
            block_fragment_confirmacao_container.visibility = View.VISIBLE
            block_fragment_title.text = "Bloqueio por roubo"
        }

        block_fragment_eu_perdi.setOnClickListener {
            block_motivo_container.visibility = View.GONE
            block_fragment_confirmacao_container.visibility = View.VISIBLE
            block_fragment_title.text = "Bloqueio por perda"
            reason = "LOSS"
        }

        block_fragment_perda_bloquear.setOnClickListener {
            block_fragment_confirmacao_container.visibility = View.GONE
            block_fragment_senha_container.visibility = View.VISIBLE
            reason = "THEFT"
        }

        block_fragment_perda_cancelar.setOnClickListener {
            mutableFlag.value = true
        }

    }

    companion object {
        lateinit var mutableFlag: MutableLiveData<Boolean>
    }


    inner class CodeDigitTextWatcher(private val parent: Int,
                                     private val view: View
    ) : TextWatcher {
        override fun afterTextChanged(value: Editable?) {
            when {
                block_fragment_input_value.text?.length == 4 -> {
                    setObservers()
                    vModel.password.set(block_fragment_input_value.text.toString())
                    vModel.cancel(reason)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

}
