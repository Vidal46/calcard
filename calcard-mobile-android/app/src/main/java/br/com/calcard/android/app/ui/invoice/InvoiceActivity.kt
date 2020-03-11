package br.com.calcard.android.app.ui.invoice

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.viewpager2.widget.ViewPager2
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.InvoiceSumary
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.FormatterUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_invoice.*


class InvoiceActivity : AppCompatActivity() {

    val vModel = InvoiceViewModel()
    var sumary = ArrayList<InvoiceSumary>()
    lateinit var viewpager: ViewPager2
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        setObservers()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        setObservers()
        vModel.getInvoiceSummary(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null))
    }

    fun setAdapter(sumarys: ObservableArrayList<InvoiceSumary>) {
        val list = arrayListOf<InvoiceSumary>()
        list.addAll(sumarys)
        val adapter: ViewPagerAdapter = ViewPagerAdapter(this, list)
        viewpager = home_view_pager
        tabLayout = tabs
        viewpager.adapter = adapter
        viewpager.setCurrentItem(getLastInvoiceClosed(sumarys))
        tabs.visibility = View.VISIBLE
        home_view_pager.visibility = View.VISIBLE
    }

    fun getLastInvoiceClosed(sumarys: ObservableArrayList<InvoiceSumary>): Int {
        var position: Int = 0
        for ((index, value) in sumarys.withIndex()) {
            if (value.status == "OPEN" && !value.paid) {
                position = index
                return position
            }
        }
        return position
    }

    fun configureViewPager() {
        TabLayoutMediator(tabs, home_view_pager, true) { tab, position ->
            tab.text = FormatterUtil.formatMonth(sumary[position].due.dueDate)
        }.attach()
    }

    fun setObservers() {

        vModel!!.sumaryOk.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                setAdapter(vModel.sumarys)
                sumary = vModel.sumarys as ArrayList<InvoiceSumary>
                configureViewPager()
            }
        })

    }

    private fun setListeners() {
        llBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

}


