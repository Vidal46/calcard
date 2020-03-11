package br.com.calcard.android.app.common.viewComponents

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.BottomSheetAlertCommonBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAlert constructor(private var icon: Drawable?, private var title: String, private var durationMillis: Long) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: BottomSheetAlertCommonBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_alert_common, container, false)
        view.commonIcon.background = icon
        view.commonTitle.text = title
        val handler = Handler()
        val runnable = Runnable { dismiss() }
        handler.postDelayed(runnable, durationMillis)
        return view.root
    }
}