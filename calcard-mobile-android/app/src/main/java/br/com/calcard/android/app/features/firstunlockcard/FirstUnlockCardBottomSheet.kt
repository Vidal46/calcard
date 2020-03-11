package br.com.calcard.android.app.features.firstunlockcard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.BottomSheetFirstUnlockCardBinding
import br.com.calcard.android.app.ui.cards.HomeActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FirstUnlockCardBottomSheet constructor(private var activity: HomeActivity, private var viewModel: FirstUnlockCardMVVM.ViewModel) : BottomSheetDialogFragment(), FirstUnlockCardMVVM.View {

    lateinit var binding: BottomSheetFirstUnlockCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_first_unlock_card, container, false)
        binding.viewModel = viewModel
        viewModel.setView(this)
        viewModel.onCreateView()
        return binding.root
    }

    override fun notifyDismissBottomSheet() {
        dismiss()
    }

    override fun notifyUnlockCardSuccess() {
        activity.reloadMenu()
    }

    override fun setCvvWatcher() {
        binding.ativarCartaoInputValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.afterCvvTextChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun setPwdWatcher() {
        binding.ativarCartaoInputValuePwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.afterPwdTextChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun notifyCvvImeActionDone() {
        binding.ativarCartaoInputValue.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }

    override fun notifyPwdImeActionDone() {
        binding.ativarCartaoInputValuePwd.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }
}