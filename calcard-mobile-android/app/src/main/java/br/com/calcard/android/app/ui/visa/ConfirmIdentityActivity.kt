package br.com.calcard.android.app.ui.visa

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.ui.ouze.BottomSheetActions
import br.com.calcard.android.app.ui.ouze.CustomBottomSheetDialogFragment
import br.com.calcard.android.app.ui.redirection.RedirectionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmIdentityActivity : AppCompatActivity() {

    private lateinit var bottomSheetFragmet: BottomSheetDialogFragment

    val viewModel = RedirectionViewModel()

    val flag = MutableLiveData<Boolean>()

    var extraObj = MigrationElegible()

    var cpfIntent = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_identity)

        intent.run {
            extraObj = getSerializableExtra("migrationEdible") as MigrationElegible
            cpfIntent = getStringExtra("cpf")
        }

        setBottomSheet()

        showBottomSheet()
    }

    private fun setBottomSheet() {
        val actions = arrayListOf<BottomSheetActions>(BottomSheetActions.CONFIRTMACAO_IDENTIDADE)
        bottomSheetFragmet = CustomBottomSheetDialogFragment.newInstance(actions)
        bottomSheetFragmet.run {
            isCancelable = false
            CustomBottomSheetDialogFragment.companionFlag = flag
            CustomBottomSheetDialogFragment.companionCpf.value = cpfIntent
            CustomBottomSheetDialogFragment.companionElegibleName.value = extraObj
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
