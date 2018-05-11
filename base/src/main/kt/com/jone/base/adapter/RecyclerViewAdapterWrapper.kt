package com.jone.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by Jone.Luo on 2017/7/7.
 */

open class RecyclerViewAdapterWrapper<VH:RecyclerView.ViewHolder>(protected val wrappedAdapter: RecyclerView.Adapter<VH>) : RecyclerView.Adapter<VH>()
{

    private val AdapterChangedCallback: AdapterDataObserverRefresh

    init
    {

        AdapterChangedCallback = AdapterDataObserverRefresh(this)

        this.wrappedAdapter.registerAdapterDataObserver(AdapterChangedCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):VH
    {
        return wrappedAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int)
    {
        wrappedAdapter.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int
    {
        return wrappedAdapter.itemCount
    }

    override fun getItemViewType(position: Int): Int
    {
        return wrappedAdapter.getItemViewType(position)
    }

    override fun setHasStableIds(hasStableIds: Boolean)
    {
        wrappedAdapter.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long
    {
        return wrappedAdapter.getItemId(position)
    }

    override fun onViewRecycled(holder: VH?)
    {
        wrappedAdapter.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: VH?): Boolean
    {
        return wrappedAdapter.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: VH?)
    {
        wrappedAdapter.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: VH?)
    {
        wrappedAdapter.onViewDetachedFromWindow(holder)
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver)
    {
        wrappedAdapter.registerAdapterDataObserver(observer)
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver)
    {
        wrappedAdapter.unregisterAdapterDataObserver(observer)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?)
    {
        wrappedAdapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?)
    {
        wrappedAdapter.onDetachedFromRecyclerView(recyclerView)
        wrappedAdapter.unregisterAdapterDataObserver(AdapterChangedCallback)
    }

    private class AdapterDataObserverRefresh(bindingRecyclerViewAdapter: RecyclerView.Adapter<*>) : RecyclerView.AdapterDataObserver()
    {
        private val adapterReference: WeakReference<RecyclerView.Adapter<*>>

        init
        {
            this.adapterReference = WeakReference<RecyclerView.Adapter<*>>(bindingRecyclerViewAdapter)
        }

        override fun onChanged()
        {
            val adapter = adapterReference.get()
            adapter?.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int)
        {
            val adapter = adapterReference.get()
            adapter?.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int)
        {
            val adapter = adapterReference.get()
            adapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int)
        {
            val adapter = adapterReference.get()
            adapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int)
        {
            val adapter = adapterReference.get()
            adapter?.notifyItemMoved(fromPosition, toPosition)
        }

    }
}


