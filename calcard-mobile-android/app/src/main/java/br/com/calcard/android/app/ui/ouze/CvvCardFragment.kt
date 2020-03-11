package br.com.calcard.android.app.ui.ouze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import br.com.calcard.android.app.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.cvv_card_layout.*


class CvvCardFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cvv_card_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {
        cvv_card_button.setOnClickListener {
            mutableFlag.value = true
        }
    }

    companion object {
        @JvmStatic
        lateinit var mutableFlag: MutableLiveData<Boolean>
    }
}
