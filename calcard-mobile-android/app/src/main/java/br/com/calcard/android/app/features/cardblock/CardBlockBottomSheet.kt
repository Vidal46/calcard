package br.com.calcard.android.app.features.cardblock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.BottomSheetCardBlockBinding
import br.com.calcard.android.app.ui.cards.HomeActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CardBlockBottomSheet constructor(private var activity: HomeActivity, private var viewModel: CardBlockMVVM.ViewModel) : BottomSheetDialogFragment(), CardBlockMVVM.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: BottomSheetCardBlockBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_card_block, container, false)
        view.viewModel = viewModel
        viewModel.setView(this)
        viewModel.onCreateView()
        return view.root
    }

    override fun notifyDismissBottomSheet() {
        dismiss()
    }

    override fun notifyCardBlockError() {
        dismiss()
        activity.showBottomSheetAlertError()
    }

    override fun notifyCardBlockSuccess() {
        activity.reloadMenu()
    }
}