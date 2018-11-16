package com.jone.base.binding.adapter

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jone.base.binding.adapter.binder.ISectionedItemTemplate
import com.jone.base.binding.command.ICommand
import com.jone.base.utils.Weak
import java.lang.ref.WeakReference

/**
 * Created by Jone.L on 2018/4/12.
 */
class BindingSectionedRecyclerViewAdapter<in TPNode, TCNode>(private val itemTemplateSectioned: ISectionedItemTemplate<TPNode, TCNode>, items: Collection<TPNode>?) : RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {
    companion object {
        private val ITEM_MODEL = -126
    }

    private val onListChangedCallback: WeakReferenceOnListChangedCallback<TPNode, TCNode>
    private var itemSectioned: ObservableList<TPNode>? = null
    private var inflater: LayoutInflater? = null
    private var itemClickCommand: ICommand? = null
    private var itemLongCLickCommand: ICommand? = null

    private var sectionForPosition: IntArray? = null
    private var positionWithinSection: IntArray? = null
    private var isHeader: BooleanArray? = null
    private var isFooter: BooleanArray? = null
    private var count = 0

    private val sectionCount: Int
        get() = if (this.itemSectioned == null) 0 else this.itemSectioned!!.size

    init {
        this.onListChangedCallback = WeakReferenceOnListChangedCallback(this)
        setItemSectioned(items)
    }


    fun setItemSectioned(itemSectioned: Collection<TPNode>?) {
        if (this.itemSectioned === itemSectioned) {
            return
        }

        if (this.itemSectioned != null) {
            this.itemSectioned!!.removeOnListChangedCallback(onListChangedCallback)
            notifyItemRangeRemoved(0, this.itemSectioned!!.size)
        }

        if (itemSectioned is ObservableList<*>) {
            this.itemSectioned = itemSectioned as ObservableList<TPNode>?
            notifyItemRangeInserted(0, this.itemSectioned!!.size)
            this.itemSectioned!!.addOnListChangedCallback(onListChangedCallback)
        } else if (itemSectioned != null) {
            this.itemSectioned = ObservableArrayList()
            this.itemSectioned!!.addOnListChangedCallback(onListChangedCallback)
            this.itemSectioned!!.addAll(itemSectioned)
        } else {
            this.itemSectioned = null
        }
        if (itemSectioned != null) {
            setupIndices()
        }
    }


    private fun setupIndices() {
        count = countItems()
        allocateAuxiliaryArrays(count)
        precomputeIndices()
    }

    private fun countItems(): Int {
        var count = 0
        val sections = sectionCount
        var itemSourceForSection: Collection<TCNode>?
        for (i in 0 until sections) {

            itemSourceForSection = itemTemplateSectioned.getItemSourceForSection(itemSectioned!![i])
            count += 1 + (if (itemSourceForSection == null) 0 else itemSourceForSection.size) + if (itemTemplateSectioned.itemTemplateSectionFooter != null) 1 else 0
        }
        return count
    }

    private fun precomputeIndices() {
        val sections = sectionCount
        var index = 0

        for (i in 0 until sections) {
            setPrecomputedItem(index, true, false, i, 0)
            index++

            for (j in 0 until itemTemplateSectioned.getItemCountForSection(itemSectioned!![i])) {
                setPrecomputedItem(index, false, false, i, j)
                index++
            }

            if (itemTemplateSectioned.itemTemplateSectionFooter != null && itemTemplateSectioned.itemTemplateSectionFooter!!.getLayoutRes(itemSectioned!![i]) > 0) {
                setPrecomputedItem(index, false, true, i, 0)
                index++
            }
        }
    }

    private fun allocateAuxiliaryArrays(count: Int) {
        sectionForPosition = IntArray(count)
        positionWithinSection = IntArray(count)
        isHeader = BooleanArray(count)
        isFooter = BooleanArray(count)
    }

    private fun setPrecomputedItem(index: Int, isHeader: Boolean, isFooter: Boolean, section: Int, position: Int) {
        this.isHeader!![index] = isHeader
        this.isFooter!![index] = isFooter
        sectionForPosition!![index] = section
        positionWithinSection!![index] = position
    }

    fun isSectionHeaderPosition(position: Int): Boolean {
        if (isHeader == null) {
            setupIndices()
        }
        return isHeader!![position]
    }

    /**
     * Returns true if the argument position corresponds to a footer
     */
    fun isSectionFooterPosition(position: Int): Boolean {
        if (isFooter == null) {
            setupIndices()
        }
        return isFooter!![position]
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        itemSectioned?.removeOnListChangedCallback(onListChangedCallback)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, layoutId: Int): BindingRecyclerViewAdapter.ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.context)
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater!!, layoutId, viewGroup, false)
        return BindingRecyclerViewAdapter.ViewHolder(binding)

    }

    override fun onBindViewHolder(viewHolder: BindingRecyclerViewAdapter.ViewHolder, position: Int) {

        val section = sectionForPosition!![position]
        val index = positionWithinSection!![position]

        val itemSection = itemSectioned!![section]
        if (isSectionHeaderPosition(position)) {//头部
            viewHolder.binding.setVariable(itemTemplateSectioned.itemTemplateSectionHeader.getBindingVariable(itemSection), itemSection)
            itemTemplateSectioned.itemTemplateSectionHeader.setVariables(viewHolder.binding, itemSection)
            viewHolder.binding.root.setTag(ITEM_MODEL, itemSection)
            if (itemTemplateSectioned.itemTemplateSectionHeader.isItemCanClick(itemSection)) {
                viewHolder.binding.root.isClickable = true
                viewHolder.binding.root.setOnClickListener(this)
                viewHolder.binding.root.setOnLongClickListener(this)
            } else {
                viewHolder.binding.root.isClickable = false
            }
        } else if (isSectionFooterPosition(position)) {//足部
            viewHolder.binding.setVariable(itemTemplateSectioned.itemTemplateSectionFooter!!.getBindingVariable(itemSection), itemSection)
            itemTemplateSectioned.itemTemplateSectionFooter!!.setVariables(viewHolder.binding, itemSection)
            viewHolder.binding.root.setTag(ITEM_MODEL, itemSection)
            if (itemTemplateSectioned.itemTemplateSectionFooter!!.isItemCanClick(itemSection)) {
                viewHolder.binding.root.isClickable = true
                viewHolder.binding.root.setOnClickListener(this)
                viewHolder.binding.root.setOnLongClickListener(this)
            } else {
                viewHolder.binding.root.isClickable = false
            }
        } else {
            val nodeList = itemTemplateSectioned.getItemSourceForSection(itemSection)
            if (nodeList != null && nodeList.size < index) {
                return
            }
            val itemChild = nodeList!![index]
            viewHolder.binding.setVariable(itemTemplateSectioned.itemTemplate.getBindingVariable(itemChild), itemChild)
            itemTemplateSectioned.itemTemplate.setVariables(viewHolder.binding, itemChild)
            viewHolder.binding.root.setTag(ITEM_MODEL, itemChild)
            if (itemTemplateSectioned.itemTemplate.isItemCanClick(itemChild)) {
                viewHolder.binding.root.isClickable = true
                viewHolder.binding.root.setOnClickListener(this)
                viewHolder.binding.root.setOnLongClickListener(this)
            } else {
                viewHolder.binding.root.isClickable = false
            }
        }

        viewHolder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {

        if (sectionForPosition == null) {
            setupIndices()
        }

        val section = sectionForPosition!![position]
        val index = positionWithinSection!![position]

        if (isSectionHeaderPosition(position)) {
            return itemTemplateSectioned.itemTemplateSectionHeader.getLayoutRes(itemSectioned!![section])
        } else if (isSectionFooterPosition(position)) {
            return itemTemplateSectioned.itemTemplateSectionHeader.getLayoutRes(itemSectioned!![section])
        } else {
            val nodeList = itemTemplateSectioned.getItemSourceForSection(itemSectioned!![section])
            return if (nodeList != null && nodeList.size < index) {
                -1
            } else itemTemplateSectioned.itemTemplate.getLayoutRes(nodeList!![index])
        }
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun onClick(v: View) {
        itemClickCommand?.apply {
            val clickItem = v.getTag(ITEM_MODEL)
            if (canExecuteAny(clickItem)) {
                executeAny(clickItem)
            }
        }
    }

    override fun onLongClick(v: View): Boolean {
        itemLongCLickCommand?.apply {
            val clickItem = v.getTag(ITEM_MODEL)
            if (canExecuteAny(clickItem)) {
                executeAny(clickItem)
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

    private class WeakReferenceOnListChangedCallback<TPNode, TCNode>(bindingRecyclerViewAdapter: BindingSectionedRecyclerViewAdapter<TPNode, TCNode>) : ObservableList.OnListChangedCallback<ObservableList<TPNode>>() {

        private val adapterReference by Weak<BindingSectionedRecyclerViewAdapter<TPNode, TCNode>> { bindingRecyclerViewAdapter }

        override fun onChanged(sender: ObservableList<TPNode>) {
            val adapter = adapterReference
            adapter?.apply {
                notifyDataSetChanged()
                setupIndices()
            }
        }

        override fun onItemRangeChanged(sender: ObservableList<TPNode>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference
            adapter?.apply {
                notifyItemRangeChanged(positionStart, itemCount)
                setupIndices()
            }
        }

        override fun onItemRangeInserted(sender: ObservableList<TPNode>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference
            adapter?.apply {
                notifyItemRangeInserted(positionStart, itemCount)
                setupIndices()
            }
        }

        override fun onItemRangeMoved(sender: ObservableList<TPNode>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            val adapter = adapterReference
            adapter?.apply {
                notifyItemMoved(fromPosition, toPosition)
                setupIndices()
            }
        }

        override fun onItemRangeRemoved(sender: ObservableList<TPNode>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference
            adapter?.apply {
                notifyItemRangeRemoved(positionStart, itemCount)
                setupIndices()
            }
        }
    }

}