package br.com.calcard.android.app.ui.visa

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.ui.redirection.MainRedirectionActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_confirm_identity_bottom_sheet.*

class ConfirmIdentityBottomSheet : BottomSheetDialogFragment() {

    private var name = String()

    private var cpf = String()

    private var phone = String()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_identity_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindCompanionObjects()

        bindViews()

        setClickListeners()
    }

    private fun bindCompanionObjects() {
        name = mutableElegible.value?.name!!

        cpf = mutableCpf.value!!

        phone = mutableElegible.value?.phone!!
    }

    private fun setClickListeners() {

        confirm_identity_fragment_decline_button.setOnClickListener {
            declineMigration()
        }

        confirm_identity_fragment_accept_button.setOnClickListener {
            acceptMigration()
        }
    }

    private fun declineMigration() {
        startActivity(Intent(activity, MainRedirectionActivity::class.java))
        activity?.finish()
    }

    private fun acceptMigration() {
        val intent = Intent(activity, FirstStepPhotoActivity::class.java)
        intent.putExtra("migrationEdible", mutableElegible.value)
        intent.putExtra("cpf", cpf)
        startActivity(intent)
    }

    private fun bindViews() {
        confirm_identity_fragment_name_value.text = name

        confirm_identity_fragment_phone_value.text = phone
    }

    companion object {
        @JvmStatic
        lateinit var mutableElegible: MutableLiveData<MigrationElegible>

        @JvmStatic
        lateinit var mutableCpf: MutableLiveData<String>
    }
}
