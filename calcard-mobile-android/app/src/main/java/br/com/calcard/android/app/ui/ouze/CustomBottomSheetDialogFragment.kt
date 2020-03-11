package br.com.calcard.android.app.ui.ouze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.calcard.android.app.R
import br.com.calcard.android.app.databinding.FragmentBottomSheetActionBinding
import br.com.calcard.android.app.model.InvoiceSumary
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.model.Spending
import br.com.calcard.android.app.model.Transaction
import br.com.calcard.android.app.ui.invoice.InvoiceDetailFragment
import br.com.calcard.android.app.ui.invoice.PaymentFragment
import br.com.calcard.android.app.ui.visa.ConfirmIdentityBottomSheet
import br.com.calcard.android.app.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("UNCHECKED_CAST")
class CustomBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetActionBinding

    private val mutableLiveDataflag = MutableLiveData<Boolean>()

    private var actions = arrayListOf<BottomSheetActions>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.fragment_bottom_sheet_action, container, false
        )

        configureActions()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDismissObserver()
    }

    private fun setDismissObserver() {
        mutableLiveDataflag.observe(this, Observer {
            dismiss()
        })
    }

    private fun configureActions() {
        arguments?.let { arguments ->
            if (arguments.containsKey(Constants.EXTRA_ACTION)) {
                actions =
                        arguments.getSerializable(Constants.EXTRA_ACTION) as ArrayList<BottomSheetActions>
                val firstAction = getFragmentFromAction(actions[0])
                replaceFragment(firstAction)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.content, fragment)
        ft.commit()
    }

    private fun getFragmentFromAction(action: BottomSheetActions): Fragment {
        return when (action) {

            BottomSheetActions.CPF -> {
                CpfRedirectionFragment().apply {
                    val args = Bundle()
                    CpfRedirectionFragment.mutableFlag = companionFlag
                    this.arguments = args
                }
            }

            BottomSheetActions.PASSWORD -> {
                PasswordMigrationFragment().apply {
                    PasswordMigrationFragment.companionElegible = companionElegible
                    PasswordMigrationFragment.companionCpf = companionCpf
                }
            }

            BottomSheetActions.CATAO_CVV -> {
                CvvCardFragment().apply {
                    CvvCardFragment.mutableFlag = mutableLiveDataflag
                }
            }
            BottomSheetActions.COMPRA -> {
                InvoiceDetailFragment().apply {
                    InvoiceDetailFragment.companionTransaction = companionTransaction
                    InvoiceDetailFragment.mutableFlag = mutableLiveDataflag
                }
            }

            BottomSheetActions.PAGAMENTO -> {
                PaymentFragment().apply {
                    PaymentFragment.mutableFlag = mutableLiveDataflag
                    PaymentFragment.companionSumary = companionSumary
                }
            }

            BottomSheetActions.BLOQUEIO -> {
                BlockCardFragment().apply {
                    BlockCardFragment.mutableFlag = mutableLiveDataflag
                }
            }

            BottomSheetActions.SENHAS -> {
                HomePasswordFragment().apply {
                    HomePasswordFragment.mutableFlag = mutableLiveDataflag
                    isCancelable = true
                }
            }

            BottomSheetActions.MEUS_DADOS -> {
                dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                MyDataFragment().apply {
                    MyDataFragment.mutableFlag = mutableLiveDataflag

                }
            }

            BottomSheetActions.TIMELINE -> {
                SpendingDetailBottomSheetFragment().apply {
                    SpendingDetailBottomSheetFragment.mutableSpending = companionSpending
                    SpendingDetailBottomSheetFragment.mutableFlag = mutableLiveDataflag
                }
            }

            BottomSheetActions.CONFIRTMACAO_IDENTIDADE -> {
                ConfirmIdentityBottomSheet().apply {
                    ConfirmIdentityBottomSheet.mutableElegible = companionElegibleName
                    ConfirmIdentityBottomSheet.mutableCpf = companionCpf
                }
            }
        }
    }

    companion object {

        fun newInstance(actions: ArrayList<BottomSheetActions>): CustomBottomSheetDialogFragment {
            val args = Bundle()
            args.putSerializable(Constants.EXTRA_ACTION, actions)
            val fragment = CustomBottomSheetDialogFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        var companionTransaction = MutableLiveData<Transaction>()

        @JvmStatic
        var companionFlag = MutableLiveData<Boolean>()

        @JvmStatic
        var companionSpending = MutableLiveData<Spending>()

        @JvmStatic
        var companionSumary = MutableLiveData<InvoiceSumary>()

        @JvmStatic
        var companionElegible = MutableLiveData<MigrationElegible>()

        @JvmStatic
        var companionCpf = MutableLiveData<String>()

        @JvmStatic
        var companionElegibleName = MutableLiveData<MigrationElegible>()
    }

}
