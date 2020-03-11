package br.com.calcard.android.app.features.goals

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.common.viewComponents.BottomSheetAlert
import br.com.calcard.android.app.databinding.ActivityGoalsBinding
import br.com.calcard.android.app.databinding.ModalInfoBinding
import br.com.calcard.android.app.features.goals.viewcomponents.GoalsBottomSheet
import br.com.calcard.android.app.features.goals.viewcomponents.GoalsInfoBottomSheet
import br.com.calcard.android.app.features.goals.viewcomponents.GoalsListAdapter
import br.com.calcard.android.app.model.Goals
import javax.inject.Inject

class GoalsActivity : AppCompatActivity(), GoalsMVVM.View {

    @Inject
    lateinit var viewModel: GoalsMVVM.ViewModel

    lateinit var binding: ActivityGoalsBinding

    lateinit var adapter: GoalsListAdapter

    private lateinit var infoBottomSheet: GoalsInfoBottomSheet
    private lateinit var bottomSheet: GoalsBottomSheet
    private lateinit var bottomSheetAlertSuccess: BottomSheetAlert
    private lateinit var bottomSheetAlertError: BottomSheetAlert
    private lateinit var alert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.getAppComponent().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goals)
        viewModel.setView(this)
        viewModel.onCreate()
    }

    override fun setAllVariables() {
        binding.viewModel = viewModel
    }

    override fun setGoalsAdapter(goals: List<Goals>?) {
        adapter = GoalsListAdapter(this, viewModel)
        binding.rvGoals.layoutManager = LinearLayoutManager(this)
        binding.rvGoals.adapter = adapter
        adapter.setItems(goals)
    }

    override fun setAllBottomSheets() {
        infoBottomSheet = GoalsInfoBottomSheet(viewModel)
        bottomSheet = GoalsBottomSheet(viewModel)
        bottomSheetAlertSuccess = BottomSheetAlert(getDrawable(R.drawable.ic_positive), getString(R.string.goals_save_success), 3000)
        bottomSheetAlertError = BottomSheetAlert(getDrawable(R.drawable.ic_negative), getString(R.string.goals_save_Error), 3000)
    }

    override fun notifyAnimateTransitionBack() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun notifyBackEvent() {
        onBackPressed()
    }

    override fun notifyOpenInfo() {
        infoBottomSheet.show(supportFragmentManager, "GoalsInfoBottomSheet")
    }

    override fun notifyDismissInfo() {
        infoBottomSheet.dismiss()
    }

    override fun notifyBottomSheet() {
        bottomSheet.show(supportFragmentManager, "GoalsBottomSheet")
    }

    override fun notifyDismissBottomSheet() {
        bottomSheet.dismiss()
    }

    override fun notifyShowError(message: String?) {
        val v: ModalInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.modal_info, null, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(v.root)
        v.tvtitle.text = getString(R.string.goals_error)
        v.tvDescribe.text = message
        v.imgClose.setOnClickListener {
            alert.dismiss()
            onBackPressed()
        }
        alert = builder.create()
        alert.show()
    }

    override fun notifyBottomSheetSuccess() {
        val handler = Handler()
        val runnable = Runnable { bottomSheetAlertSuccess.show(supportFragmentManager, "BottomSheetAlertSuccess") }
        handler.postDelayed(runnable, 500)
    }

    override fun notifyBottomSheetError() {
        bottomSheet.dismiss()
        val handler = Handler()
        val runnable = Runnable { bottomSheetAlertError.show(supportFragmentManager, "BottomSheetAlertError") }
        handler.postDelayed(runnable, 500)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.onBackPressed()
    }
}