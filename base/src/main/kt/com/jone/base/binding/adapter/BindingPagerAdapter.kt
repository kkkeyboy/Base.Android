package com.jone.base.binding.adapter

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.jone.base.utils.Weak

class BindingPagerAdapter(fm: FragmentManager, items: Collection<Fragment>) : FragmentStatePagerAdapter(fm) {

    private val onListChangedCallback: WeakReferenceOnListChangedCallback<Fragment>
    private val items: ObservableList<Fragment>

    init {
        this.onListChangedCallback = WeakReferenceOnListChangedCallback(this)
        this.items = (if (items is ObservableList<*>) {
            items as ObservableList
        } else  {
         ObservableArrayList<Fragment>().apply { addAll(items) }
        }).apply {
            addOnListChangedCallback(onListChangedCallback)
        }

    }

    override fun getItem(position: Int): Fragment {
       return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    private class WeakReferenceOnListChangedCallback<T>(adapter: BindingPagerAdapter) : ObservableList.OnListChangedCallback<ObservableList<T>>() {

        private val adapterReference by Weak<BindingPagerAdapter> { adapter }

        override fun onChanged(sender: ObservableList<T>) {
            val adapter = adapterReference

            if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                adapter?.notifyDataSetChanged()
            } else { // 非UI主线程
                Handler(Looper.getMainLooper()).post {
                    adapter?.notifyDataSetChanged()
                }
            }

        }

        override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference

            if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                adapter?.notifyDataSetChanged()
            } else { // 非UI主线程
                Handler(Looper.getMainLooper()).post {
                    adapter?.notifyDataSetChanged()
                }
            }

        }

        override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference
            adapter?.apply {
                if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                    adapter.notifyDataSetChanged()
                } else { // 非UI主线程
                    Handler(Looper.getMainLooper()).post {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            val adapter = adapterReference
            if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                adapter?.notifyDataSetChanged()
            } else { // 非UI主线程
                Handler(Looper.getMainLooper()).post {
                    adapter?.notifyDataSetChanged()
                }
            }

        }

        override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference
            adapter?.apply {
                if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                    notifyDataSetChanged()
                } else { // 非UI主线程
                    Handler(Looper.getMainLooper()).post {
                        notifyDataSetChanged()
                    }
                }

            }
        }
    }
}