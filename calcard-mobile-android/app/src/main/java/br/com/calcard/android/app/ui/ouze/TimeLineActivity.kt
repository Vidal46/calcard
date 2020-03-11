package br.com.calcard.android.app.ui.ouze

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Spending
import br.com.calcard.android.app.model.Timeline
import br.com.calcard.android.app.ui.cards.MainViewModel
import br.com.calcard.android.app.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView
import kotlinx.android.synthetic.main.activity_time_line.*
import java.util.*

class TimeLineActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    private var timeline = MutableLiveData<Timeline>()

    lateinit var bottomSheet: BottomSheetDialogFragment

    lateinit var layoutManager: LinearLayoutManager

    var spendingList = MutableLiveData<ArrayList<Spending>>()

    lateinit var adapter: SpendingAdapter

    var maxPage = 0

    var page = 0

    var isLoading = false

    private val mainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        bindViews()
        setObserver()
        callTimeline()

        val shadowView: MaterialShadowContainerView = activity_time_line_constraint
        val density = resources.displayMetrics.density
        shadowView.shadowTranslationZ = density * 3.0f
        shadowView.shadowElevation = density * 5.0f
    }

    private fun setBottomSheet(spending: Spending) {
        val actions = arrayListOf<BottomSheetActions>(BottomSheetActions.TIMELINE)
        bottomSheet = CustomBottomSheetDialogFragment.newInstance(actions)
        bottomSheet.run {
            CustomBottomSheetDialogFragment.companionSpending.value = spending
        }

    }

    private fun callTimeline() {

        showProgress()
        mainViewModel.spendings.clear()
        val data = HashMap<String, String>()
        data["page"] = page.toString()
        mainViewModel.getPagedTimeLine(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), data)
    }

    private fun showProgress() {
        activity_time_line_progress.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        activity_time_line_progress.visibility = View.GONE
    }

    private fun setObserver() {
        mainViewModel.mutableTimeLine.observe(this, Observer {
            maxPage = it.totalPages
            timeline.value = it
            fillList(it.spendings)
        })

        spendingList.observe(this, Observer {
            setListener(it)
            getPage(it)
        })
        ll_back_arrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fillList(spendings: List<Spending>) {
        if (spendingList.value.isNullOrEmpty()) {
            spendingList.value = spendings as ArrayList
        } else {
            for (item in spendings) {
                spendingList.value!!.add(item)
            }
            getPage(spendings as ArrayList<Spending>)
        }
    }

    private fun setListener(spendings: ArrayList<Spending>) {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount

                if (!isLoading) {
                    if (visibleItemCount + pastVisibleItem >= total && maxPage > page) {
                        getPage(spendings)
                        page++

                        callTimeline()
                    }
                }

                super.onScrolled(recyclerView, 1, 2)
            }

        })
    }

    private fun getPage(spendings: ArrayList<Spending>) {
        isLoading = true

        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
            dismissProgress()
        } else {
            setAdapter(spendings)
        }

        isLoading = false
    }

    private fun bindViews() {
        recyclerView = activity_time_line_recycler
    }

    private fun setAdapter(list: List<Spending>) {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = SpendingAdapter(this@TimeLineActivity, list) {
            setBottomSheet(it)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        dismissProgress()
    }


}
