package br.com.calcard.android.app.features.unlockcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.BottomSheetUnlockCardBinding
import br.com.calcard.android.app.ui.cards.HomeActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UnlockCardBottomSheet constructor(private var activity: HomeActivity, private var viewModel: UnlockCardMVVM.ViewModel) : BottomSheetDialogFragment(), UnlockCardMVVM.View {

    lateinit var binding: BottomSheetUnlockCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_unlock_card, container, false)
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

    override fun notifyUnlockCardError() {
        dismiss()
        activity.showBottomSheetAlertError()
    }
}