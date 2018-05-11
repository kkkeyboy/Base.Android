package com.jone.base.adapter;

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup

/**
 * Created by Jone.Luo on 2017/7/7.
 * 带header和footer的recyclerView adapter封装
 */

class RecyclerHeaderAndFooterAdapter<VH:RecyclerView.ViewHolder>(targetAdapter: RecyclerView.Adapter<VH>) : RecyclerViewAdapterWrapper<VH>(targetAdapter)
{

    private var layoutManager: RecyclerView.LayoutManager? = null

    private var headerView: View? = null
    private var footerView: View? = null

    fun setHeaderView(view: View): RecyclerHeaderAndFooterAdapter<VH>
    {
        headerView = view
        wrappedAdapter.notifyDataSetChanged()
        return this
    }

    fun removeHeaderView()
    {
        headerView = null
        wrappedAdapter.notifyDataSetChanged()
    }

    fun setFooterView(view: View): RecyclerHeaderAndFooterAdapter<VH>
    {
        footerView = view
        wrappedAdapter.notifyDataSetChanged()
        return this
    }

    fun removeFooterView()
    {
        footerView = null
        wrappedAdapter.notifyDataSetChanged()
    }

    private fun setGridHeaderFooter(layoutManager: RecyclerView.LayoutManager?)
    {
        if (layoutManager is GridLayoutManager)
        {
            val gridLayoutManager = layoutManager as GridLayoutManager?
            gridLayoutManager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup()
            {
                override fun getSpanSize(position: Int): Int
                {
                    val isShowHeader = position == 0 && hasHeader()
                    val isShowFooter = position == itemCount - 1 && hasFooter()
                    return if (isShowFooter || isShowHeader){ gridLayoutManager.spanCount} else 1
                }
            }
        }
    }

    private fun hasHeader(): Boolean
    {
        return headerView != null
    }

    private fun hasFooter(): Boolean
    {
        return footerView != null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?)
    {
        super.onAttachedToRecyclerView(recyclerView)
        layoutManager = recyclerView!!.layoutManager
        setGridHeaderFooter(layoutManager)
    }

    override fun getItemCount(): Int
    {
        return super.getItemCount() + (if (hasHeader()) 1 else 0) + if (hasFooter()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int
    {
        if (hasHeader() && position == 0)
        {
            return TYPE_HEADER
        }

        return if (hasFooter() && position == itemCount - 1)
        {
            TYPE_FOOTER
        } else super.getItemViewType(if (hasHeader()) position - 1 else position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    {
        var itemView: View? = null
        if (viewType == TYPE_HEADER)
        {
            itemView = headerView
        } else if (viewType == TYPE_FOOTER)
        {
            itemView = footerView
        }
        if (itemView != null)
        {
            //set StaggeredGridLayoutManager header & footer view
            if (layoutManager is StaggeredGridLayoutManager)
            {
                val targetParams = itemView.layoutParams
                val StaggerLayoutParams: StaggeredGridLayoutManager.LayoutParams
                if (targetParams != null)
                {
                    StaggerLayoutParams = StaggeredGridLayoutManager.LayoutParams(targetParams.width, targetParams.height)
                } else
                {
                    StaggerLayoutParams = StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                StaggerLayoutParams.isFullSpan = true
                itemView.layoutParams = StaggerLayoutParams
            }
            @Suppress("UNCHECKED_CAST")
            return object : RecyclerView.ViewHolder(itemView!!)
            {

            }  as VH
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int)
    {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
        {
            //if you need get header & footer state , do here
            return
        }
        super.onBindViewHolder(holder, if (hasHeader()) position - 1 else position)
    }

   private companion object
    {
        private val TYPE_HEADER = -1
        private val TYPE_FOOTER = -2
    }
}
