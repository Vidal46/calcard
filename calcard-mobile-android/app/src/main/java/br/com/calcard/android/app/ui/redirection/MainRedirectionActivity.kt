package br.com.calcard.android.app.ui.redirection

import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.ouze.BottomSheetActions
import br.com.calcard.android.app.ui.ouze.CustomBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainRedirectionActivity : AppCompatActivity() {
    val activityContext = this@MainRedirectionActivity

    private lateinit var bottomSheetFragmet: BottomSheetDialogFragment

    val viewModel = RedirectionViewModel()

    val flag = MutableLiveData<Boolean>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_redirection)

        setBottomSheet()

        showBottomSheet()
    }

    private fun setBottomSheet() {
        val actions = arrayListOf<BottomSheetActions>(BottomSheetActions.CPF)
        bottomSheetFragmet = CustomBottomSheetDialogFragment.newInstance(actions)
        bottomSheetFragmet.run {
            isCancelable = false
            CustomBottomSheetDialogFragment.companionFlag = flag
        }

        flag.observe(this, Observer {
            bottomSheetFragmet.dismiss()
        })
    }

    private fun showBottomSheet() {
        Handler().postDelayed({
            bottomSheetFragmet.show(supportFragmentManager, bottomSheetFragmet.tag)
        }, 500)
    }
}
