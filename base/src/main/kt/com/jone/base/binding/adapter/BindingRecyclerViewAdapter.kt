package com.jone.base.binding.adapter

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jone.base.binding.adapter.binder.IItemTemplate
import com.jone.base.binding.command.ICommand
import com.jone.base.utils.Weak

/**
 * Created by Jone.L on 2017/3/12.
 * 常用的recyclerView adapter
 */

open class BindingRecyclerViewAdapter<T>(private val itemTemplate: IItemTemplate<T>, items: Collection<T>?) : RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {
    companion object {
        private const val ITEM_POSITION = -124
    }

    private val onListChangedCallback: WeakReferenceOnListChangedCallback<T>
    var items: ObservableList<T>? = null
        private set
    private var inflater: LayoutInflater? = null
    private var itemClickCommand: ICommand? = null
    private var itemLongCLickCommand: ICommand? = null

    init {
        this.onListChangedCallback = WeakReferenceOnListChangedCallback(this)
        setItems(items)
    }

    fun setItems(items: Collection<T>?) {
        if (this.items === items) {
            return
        }

        this.items?.apply {
            removeOnListChangedCallback(onListChangedCallback)
            notifyItemRangeRemoved(0, size)
        }

        if (items is ObservableList<*>) {
            this.items = items as ObservableList<T>?
            notifyItemRangeInserted(0, this.items!!.size)
            this.items!!.addOnListChangedCallback(onListChangedCallback)
        } else if (items != null) {
            this.items = ObservableArrayList()
            this.items!!.apply {
                addOnListChangedCallback(onListChangedCallback)
                addAll(items)
            }
        } else {
            this.items = null
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        items?.apply { removeOnListChangedCallback(onListChangedCallback) }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, layoutId: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.context)
        }

        //        ViewDataBinding binding = DataBindingUtil.bind(View.inflate(viewGroup.getContext(), layoutId, viewGroup));
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater!!, layoutId, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items!![position]
        viewHolder.binding.setVariable(itemTemplate.getBindingVariable(item), item)
        itemTemplate.setVariables(viewHolder.binding, item)
        viewHolder.binding.root.setTag(ITEM_POSITION, position)
        if (itemTemplate.isItemCanClick(item)) {
            viewHolder.binding.root.isClickable = true
            viewHolder.binding.root.setOnClickListener(this)
            viewHolder.binding.root.setOnLongClickListener(this)
        } else {
            viewHolder.binding.root.isClickable = false
        }
        viewHolder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return itemTemplate.getLayoutRes(items!![position])
    }

    override fun getItemCount() = items?.size ?: 0

    override fun onClick(v: View) {
        itemClickCommand?.apply {
            val item = items!![v.getTag(ITEM_POSITION) as Int]
            if (canExecuteAny(item)) {
                executeAny(item)
            }
        }
    }

    override fun onLongClick(v: View): Boolean {
        itemLongCLickCommand?.apply {
            val item = items!![v.getTag(ITEM_POSITION) as Int]
            if (canExecuteAny(item)) {
                executeAny(item)
            }
            return true
        }
        return false
    }

    fun setClickHandler(itemClickHandler: ICommand) {
        this.itemClickCommand = itemClickHandler
    }

    fun setLongClickHandler(itemLongCLickHandler: ICommand) {
        this.itemLongCLickCommand = itemLongCLickHandler
    }

    class ViewHolder(internal val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    private class WeakReferenceOnListChangedCallback<T>(bindingRecyclerViewAdapter: BindingRecyclerViewAdapter<T>) : ObservableList.OnListChangedCallback<ObservableList<T>>() {

        private val adapterReference by Weak<BindingRecyclerViewAdapter<T>> { bindingRecyclerViewAdapter }

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
                adapter?.notifyItemRangeChanged(positionStart, itemCount)
            } else { // 非UI主线程
                Handler(Looper.getMainLooper()).post {
                    adapter?.notifyItemRangeChanged(positionStart, itemCount)
                }
            }

        }

        override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference
            adapter?.apply {
                if (sender.isEmpty() || sender.size == itemCount && positionStart == 0) {
                    if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                        notifyDataSetChanged()
                    } else { // 非UI主线程
                        Handler(Looper.getMainLooper()).post {
                            notifyDataSetChanged()
                        }
                    }

                    return
                }

                if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                    notifyItemRangeInserted(positionStart, itemCount)
                } else { // 非UI主线程
                    Handler(Looper.getMainLooper()).post {
                        notifyItemRangeInserted(positionStart, itemCount)
                    }
                }
            }
        }

        override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            val adapter = adapterReference

            if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                adapter?.notifyItemMoved(fromPosition, toPosition)
            } else { // 非UI主线程
                Handler(Looper.getMainLooper()).post {
                    adapter?.notifyItemMoved(fromPosition, toPosition)
                }
            }

        }

        override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference
            adapter?.apply {
                if (sender.isEmpty() && positionStart == 0 && itemCount > 0) {
                    if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                        notifyDataSetChanged()
                    } else { // 非UI主线程
                        Handler(Looper.getMainLooper()).post {
                            notifyDataSetChanged()
                        }
                    }
                    return
                }
                if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
                    notifyItemRangeRemoved(positionStart, itemCount)
                } else { // 非UI主线程
                    Handler(Looper.getMainLooper()).post {
                        notifyItemRangeRemoved(positionStart, itemCount)
                    }
                }

            }
        }
    }

}