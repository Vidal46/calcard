package br.com.calcard.android.app.ui.invoice

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.calcard.android.app.model.InvoiceSumary

class ViewPagerAdapter(fragment: FragmentActivity,val sumarys: ArrayList<InvoiceSumary>) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return sumarys.size
    }

    override fun createFragment(position: Int): Fragment {
        return OuzeInvoiceFragment(sumarys[position])
    }

}