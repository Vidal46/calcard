package br.com.calcard.android.app.ui.cards

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.adapter.HomeMenuAdapter
import br.com.calcard.android.app.common.viewComponents.BottomSheetAlert
import br.com.calcard.android.app.databinding.ActivityHomeBinding
import br.com.calcard.android.app.features.cardblock.CardBlockBottomSheet
import br.com.calcard.android.app.features.cardblock.CardBlockMVVM
import br.com.calcard.android.app.features.firstunlockcard.FirstUnlockCardBottomSheet
import br.com.calcard.android.app.features.firstunlockcard.FirstUnlockCardMVVM
import br.com.calcard.android.app.features.goals.GoalsActivity
import br.com.calcard.android.app.features.unlockcard.UnlockCardBottomSheet
import br.com.calcard.android.app.features.unlockcard.UnlockCardMVVM
import br.com.calcard.android.app.model.HomeMenuModel
import br.com.calcard.android.app.ui.invoice.InvoiceActivity
import br.com.calcard.android.app.ui.login.LoginRedirectionActivity
import br.com.calcard.android.app.ui.ouze.BottomSheetActions
import br.com.calcard.android.app.ui.ouze.CustomBottomSheetDialogFragment
import br.com.calcard.android.app.ui.ouze.HomeItemEnum
import br.com.calcard.android.app.ui.ouze.HomeItemEnum.*
import br.com.calcard.android.app.ui.ouze.TimeLineActivity
import br.com.calcard.android.app.ui.ouze_insurance.HomeInsuranceActivity
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.FormatterUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), MainCardModelCallback {

    private lateinit var bottomSheetFragmet: BottomSheetDialogFragment

    private lateinit var cardBlockBottomSheet: CardBlockBottomSheet
    private lateinit var firstUnlockCardBottomSheet: FirstUnlockCardBottomSheet
    private lateinit var unlockCardBottomSheet: UnlockCardBottomSheet
    private lateinit var alertBottomSheetError: BottomSheetAlert

    private var flag = MutableLiveData<Boolean>()

    private lateinit var scrollView: ScrollView

    private lateinit var binding: ActivityHomeBinding

    private lateinit var adapter: HomeMenuAdapter

    @Inject
    lateinit var cardBlockViewModel: CardBlockMVVM.ViewModel

    @Inject
    lateinit var firstUnlockCardViewModel: FirstUnlockCardMVVM.ViewModel
    @Inject
    lateinit var unlockCardViewModel: UnlockCardMVVM.ViewModel


    private val mViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val cardViewModel: MainCardModel by lazy {
        ViewModelProviders.of(this).get(MainCardModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.getAppComponent().inject(this)
        setContentView(R.layout.activity_home)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setAdapter(HomeMenuModel().itens)

        setObservers()

        setBottomSheet()

        initAllActionSheet()

        redirectToTimelineListener()

        viewModelDataCall()

        setClickListener()
    }

    private fun initAllActionSheet() {
        alertBottomSheetError = BottomSheetAlert(getDrawable(R.drawable.ic_negative), getString(R.string.goals_save_Error), 3000)
        cardBlockBottomSheet = CardBlockBottomSheet(this, cardBlockViewModel)
        firstUnlockCardBottomSheet = FirstUnlockCardBottomSheet(this, firstUnlockCardViewModel)
        unlockCardBottomSheet = UnlockCardBottomSheet(this, unlockCardViewModel)
    }

    private fun viewModelDataCall() {
        mViewModel.spendings.clear()
        cardViewModel.profileApi(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null))
        cardViewModel.getAccounts(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null))
    }

    override fun onResume() {
        super.onResume()

        adjustScrollView()
    }

    private fun redirectToTimelineListener() {
        binding.homeActivityLastbuyContainer?.setOnClickListener {
            startActivity(Intent(this, TimeLineActivity::class.java))
        }
    }

    private fun adjustScrollView() {
        if (home_activity_scrollview != null) {
            binding.homeActivityScrollview?.isFocusableInTouchMode = true

            binding.homeActivityScrollview?.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        }
    }

    private fun setAdapter(itens: ArrayList<HomeItemEnum>) {
        val recyclerView = findViewById<RecyclerView>(R.id.home_menu_recycler)
        adapter = HomeMenuAdapter(this, itens) { it ->

            val actions = arrayListOf<BottomSheetActions>()

            when (it) {
                DESBLOQUEAR_CARTAO -> {
                    val status = MyApplication.preferences.getBoolean("firstUnlock", false)
                    if (status){
                        firstUnlockCardBottomSheet.show(supportFragmentManager, "FirstUnlockCardBottomSheet")
                    } else {
                        unlockCardBottomSheet.show(supportFragmentManager, "UnlockCardBottomSheet")
                    }
                }

                BLOQUEAR_CARTAO -> {
                    cardBlockBottomSheet.show(supportFragmentManager, "CardBlockBottomSheet")
                }

                SAIR -> setDialog()

                MINHA_FATURA -> {
                    val intent = Intent(this, InvoiceActivity::class.java)
                    startActivity(intent)
                }

                PERDA_E_ROUBO -> {
                    setBlockBottomSheet()
                    showBottomSheet()
                }

                TIMELINE -> {
                    startActivity(Intent(this, TimeLineActivity::class.java))
                }

                MINHAS_SENHAS -> {
                    actions.add(BottomSheetActions.SENHAS)
                    bottomSheetFragmet = CustomBottomSheetDialogFragment.newInstance(actions)

                    showBottomSheet()
                }

                MEUS_DADOS -> {
                    actions.add(BottomSheetActions.MEUS_DADOS)
                    bottomSheetFragmet = CustomBottomSheetDialogFragment.newInstance(actions)

                    showBottomSheet()
                }

                METAS -> {
                    val intent = Intent(this, GoalsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

//                SEGUROS_ASSISTENCIAS -> {
//                    startActivity(Intent(this, HomeInsuranceActivity::class.java))
//                }

            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showBottomSheet() {
        bottomSheetFragmet.show(supportFragmentManager, bottomSheetFragmet.tag)
    }

    private fun clearApp() {
        val intent = Intent(this, LoginRedirectionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun setObservers() {
        mViewModel!!.rapido.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {

                home_progressbar.setMax(Math.round(mViewModel!!.withdraw.get()!!).toInt())
                home_progressbar.setProgress(Math.round(mViewModel!!.used.get()!!).toInt())
                home_limite_disponivel.text = mViewModel!!.avaliable.get()
                binding.homeLimiteTotalValue?.text = "${FormatterUtil.formatCurrency(mViewModel!!.withdraw.get())}"
                binding.homeLimiteGastoValue?.text = "${FormatterUtil.formatCurrency(mViewModel!!.used.get())}"
                home_saque_rapido.text = mViewModel.rapido.get()


                mViewModel!!.getInvoiceSummary(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null))

            }

        })

        mViewModel!!.bestDayToBuy.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                home_melhor_dia.text = "Dia " + mViewModel!!.bestDayToBuy.get()!!.split('-')[2]
            }
        })

        mViewModel!!.dueDate.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {

                home_vencimento.text = FormatterUtil.formatDate(mViewModel!!.dueDate.get()!!)
                mViewModel!!.getLastTransaction(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null),
                        MyApplication.preferences.getString("idAccount", null))
            }
        })

        mViewModel!!.detailsOk.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {

                if (mViewModel.lastTransaction != null) {
                    val lastTransaction = mViewModel.lastTransaction
                    val iconString = lastTransaction.expenseGoal?.name ?: "Outros"

                    home_ultima_compra.text = "Sua última compra foi em " + lastTransaction.establishment + " no valor de " + FormatterUtil.formatCurrency(lastTransaction.value) + ", em " + FormatterUtil.formatFullDate(lastTransaction.eventDate)
                    home_container_progressbar.visibility = View.GONE
                    home_container_interno.visibility = View.VISIBLE
                    home_ultima_compra_icon.setImageResource(getIcon(iconString))

                } else {
                    home_ultima_compra.text = "Você ainda não realizou nenhuma compra"
                    home_container_progressbar.visibility = View.GONE
                    home_container_interno.visibility = View.VISIBLE
                    home_activity_lastbuy_container.visibility = View.GONE
                }

            }
        })

        mViewModel.error.observe(this, Observer {
            it?.let { error ->
                home_container_progressbar.visibility = View.GONE
                home_container_error_img.visibility = View.VISIBLE
            }
        })

        cardViewModel.setMainCardModelCallback(this)
        cardViewModel.idAccount.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.getLimits(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null))
            }
        })

    }

    private fun setBottomSheet() {
        val actions = arrayListOf<BottomSheetActions>(BottomSheetActions.CPF)
        bottomSheetFragmet = CustomBottomSheetDialogFragment.newInstance(actions)
        bottomSheetFragmet.run {
            isCancelable = false
        }
    }

    private fun setBlockBottomSheet() {
        val actions = arrayListOf<BottomSheetActions>(BottomSheetActions.BLOQUEIO)
        bottomSheetFragmet = CustomBottomSheetDialogFragment.newInstance(actions)
        bottomSheetFragmet.run {

        }

        flag.observe(this, Observer {
            bottomSheetFragmet.dismiss()
        })

    }

    fun getIcon(tipo: String): Int {
        return when (tipo.toLowerCase()) {
            "mercado" -> R.drawable.ic_supermercado

            "restaurante" -> R.drawable.ic_restaurante

            "lazer" -> R.drawable.ic_lazer

            "saude" -> R.drawable.ic_saude

            "transporte" -> R.drawable.ic_transporte

            else -> {
                R.drawable.ic_outros
            }
        }

    }

    fun setClickListener() {
        home_container_error_img.setOnClickListener {
            home_container_progressbar.visibility = View.VISIBLE
            home_container_error_img.visibility = View.GONE
            mViewModel.spendings.clear();
            cardViewModel.profileApi(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
            cardViewModel.getAccounts(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
            mViewModel.getLimits(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
        }
    }

    fun setDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Tem certeza que deseja sair?")
        alertDialogBuilder.setPositiveButton("Sim,quero sair") { dialog, wich ->
            clearApp()
        }
        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }

        val alert = alertDialogBuilder.create()
        alert.show()
    }

    fun showBottomSheetAlertError() {
        val handler = Handler()
        val runnable = Runnable { alertBottomSheetError.show(supportFragmentManager, "BottomSheetAlertError") }
        handler.postDelayed(runnable, 500)
    }

    fun reloadMenu() {
        cardViewModel.getAccounts(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
    }

    override fun onBackPressed() {
        setDialog()
    }

    override fun notifyReloadMenu() {
        val status = MyApplication.preferences.getBoolean("allowsLock", false)
        adapter.itens.remove(BLOQUEAR_CARTAO)
        adapter.itens.remove(DESBLOQUEAR_CARTAO)
        adapter.notifyDataSetChanged()
        if (status) {
            adapter.itens.add(4, BLOQUEAR_CARTAO)
            adapter.notifyItemInserted(4)
        } else {
            adapter.itens.add(4, DESBLOQUEAR_CARTAO)
            adapter.notifyItemInserted(4)
        }
    }
}