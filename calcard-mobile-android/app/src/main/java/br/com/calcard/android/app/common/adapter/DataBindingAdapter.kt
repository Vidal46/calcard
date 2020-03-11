package br.com.calcard.android.app.common.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class DataBindingAdapter<T : ViewDataBinding?, I> protected constructor(activity: Activity) : RecyclerView.Adapter<BindingViewHolder<T>?>() {

    private val inflater: LayoutInflater = LayoutInflater.from(activity)
    private var items: MutableList<I> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<T> {
        val binding: T = DataBindingUtil.inflate<T>(inflater, itemResource, parent, false)
        return BindingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<I>?) {
        this.items.clear()
        this.items.addAll(items!!)
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<I> {
        return items
    }

    @get:LayoutRes
    protected abstract val itemResource: Int
}

