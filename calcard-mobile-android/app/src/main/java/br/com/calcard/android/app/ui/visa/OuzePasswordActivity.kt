package br.com.calcard.android.app.ui.visa

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.ActivityOuzePasswordBinding
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.ui.ouze.BottomSheetActions
import br.com.calcard.android.app.ui.ouze.CustomBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OuzePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityOuzePasswordBinding

    lateinit var bottomSheetFragment: BottomSheetDialogFragment

    private var checkBool = false

    private var password = String()

    private var firstPassword = String()

    private var secondPassword = String()

    var cpfIntent = String()

    var extraObj = MigrationElegible()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ouze_password)

        intent.run {
            extraObj = getSerializableExtra("migrationEdible") as MigrationElegible
            cpfIntent = getStringExtra("cpf")
        }

        setBottomSheet()
        showBottomSheet()
    }

    private fun setBottomSheet() {
        val action = arrayListOf<BottomSheetActions>(BottomSheetActions.PASSWORD)

        bottomSheetFragment = CustomBottomSheetDialogFragment.newInstance(action)
        bottomSheetFragment.run {
            isCancelable = false

            CustomBottomSheetDialogFragment.companionElegible.value = extraObj
            CustomBottomSheetDialogFragment.companionCpf.value = cpfIntent
        }
    }

    private fun showBottomSheet() {
        Handler().postDelayed({
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }, 500)
    }
}