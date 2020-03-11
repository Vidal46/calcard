package br.com.calcard.android.app.features.goals.viewcomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.BottomSheetGoalsInfoBinding
import br.com.calcard.android.app.features.goals.GoalsMVVM
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GoalsInfoBottomSheet constructor(private var viewModel: GoalsMVVM.ViewModel) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: BottomSheetGoalsInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_goals_info, container, false)
        view.viewModel = viewModel
        return view.root
    }
}