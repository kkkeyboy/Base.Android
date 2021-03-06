package com.jone.base.binding

import android.content.Context
import android.databinding.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Selection
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.jone.base.adapter.RecyclerHeaderAndFooterAdapter
import com.jone.base.binding.adapter.BindingPagerAdapter
import com.jone.base.binding.adapter.BindingRecyclerViewAdapter
import com.jone.base.binding.adapter.BindingSectionedRecyclerViewAdapter
import com.jone.base.binding.adapter.binder.IItemTemplate
import com.jone.base.binding.adapter.binder.ISectionedItemTemplate
import com.jone.base.binding.adapter.binder.ItemTemplate
import com.jone.base.binding.command.BaseCommandWithContext
import com.jone.base.binding.command.ICommand


/**
 * Created by Jone.L on 2017/11/30.
 */
object CustomViewBindings {
    //region Command
    @JvmStatic
    @BindingAdapter(value = *arrayOf("command", "commandParameter"), requireAll = false)
    fun bindViewCommandWithParameter(view: View, command: ICommand, commandParameter: Any?) {
        if (command is BaseCommandWithContext<*>) {
            command.ctx = view.context
        }
        view.setOnClickListener { _ ->
            if (command.canExecuteAny(commandParameter)) {
                command.executeAny(commandParameter)
            }
        }
    }

    //    //endregion

    //region RecyclerView

    //region 普通RecyclerView

    @JvmStatic
    @BindingAdapter(value = [
        "itemSource",
        "itemTemplate",
        "itemClickCommand",
        "itemLongClickCommand",
        "header",
        "headerBR",
        "headerViewModel",
        "footer",
        "footerBR",
        "footerViewModel"],
            requireAll = false)
    fun <T> bindRecyclerViewBinder(recyclerView: RecyclerView,
                                   itemSource: Collection<T>,
                                   itemTemplate: IItemTemplate<T>,
                                   clickHandler: ICommand?,
                                   longClickHandler: ICommand?,
                                   headerLayout: Int,
                                   headerBR: Int,
                                   headerViewModel: Any?,
                                   footerLayout: Int,
                                   footerBR: Int,
                                   footerViewModel: Any?) {
        if (recyclerView.layoutManager == null) {
            //            throw new NullPointerException("Not find recyclerView's layoutManager");
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        }

        val adapter = BindingRecyclerViewAdapter(itemTemplate, itemSource)
        if (clickHandler != null) {
            if(clickHandler is BaseCommandWithContext<*>)
            {
                clickHandler.ctx = recyclerView.context
            }
            adapter.setClickHandler(clickHandler)
        }
        if (longClickHandler != null) {
            if(longClickHandler is BaseCommandWithContext<*>)
            {
                longClickHandler.ctx = recyclerView.context
            }
            adapter.setLongClickHandler(longClickHandler)
        }

        var headerAndFooterAdapter: RecyclerHeaderAndFooterAdapter<*>? = null
        if (headerLayout > 0 || footerLayout > 0) {
            headerAndFooterAdapter = RecyclerHeaderAndFooterAdapter(adapter)
            if (headerLayout > 0) {
                var headerView: View?
                if (headerViewModel != null) {
                    val bindingHeader = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(recyclerView.context), headerLayout, null, false)
                    bindingHeader.setVariable(headerBR, headerViewModel)
                    headerView = bindingHeader.root

                } else {
                    headerView = View.inflate(recyclerView.context, headerLayout, null)
                }
                if (headerView!!.layoutParams == null) {
                    headerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                headerAndFooterAdapter.setHeaderView(headerView)
            }

            if (footerLayout > 0) {
                var footerView: View?
                if (footerViewModel != null) {
                    val bindingFooter = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(recyclerView.context), footerLayout, null, false)
                    bindingFooter.setVariable(footerBR, footerViewModel)
                    footerView = bindingFooter.root

                } else {
                    footerView = View.inflate(recyclerView.context, footerLayout, null)
                }
                if (footerView!!.layoutParams == null) {
                    footerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                headerAndFooterAdapter.setFooterView(footerView)
            }
        }

        recyclerView.adapter = if (headerAndFooterAdapter == null) adapter else headerAndFooterAdapter
    }


   /* @JvmStatic
    @BindingAdapter(value = [
        "itemSource",
        "itemTemplate",
        "itemTemplateBR",
        "itemClickCommand",
        "header",
        "headerBR",
        "headerViewModel",
        "footerLayout",
        "footerBR",
        "footerViewModel",
        "variableViewModel",
        "variableBR"], requireAll = false)
    fun <T> bindRecyclerViewBinderWithClickHasHeaderFooter(view: RecyclerView,
                                                           itemSource: Collection<T>,
                                                           itemTemplateId: Int,
                                                           itemTemplateBR: Int,
                                                           itemClickCommand: ICommand,
                                                           headerLayout: Int,
                                                           headerBR: Int,
                                                           headerViewModel: Any,
                                                           footerLayout: Int,
                                                           footerBR: Int,
                                                           footerViewModel: Any,
                                                           variableViewModel: Any?,
                                                           variableBR: Int) {
        bindRecyclerViewBinder(view, itemSource, object : ItemTemplate<T>(itemTemplateBR, itemTemplateId) {
            override fun setVariables(binding: ViewDataBinding, item: T) {
                if (variableViewModel != null) {
                    binding.setVariable(variableBR, variableViewModel)
                }
            }
        }, itemClickCommand, null, headerLayout, headerBR, headerViewModel, footerLayout, footerBR, footerViewModel)
    }*/

    //endregion


    //region 分片式的（数据源式包含关系但是展现是在一级
    @JvmStatic
    @BindingAdapter(value = *arrayOf("itemSource", "itemTemplate", "itemClickCommand", "itemLongClickCommand"), requireAll = false)
    fun <T, TC> bindRecyclerViewBinder(view: RecyclerView,
                                       itemSource: Collection<T>,
                                       itemTemplate: ISectionedItemTemplate<T, TC>,
                                       clickHandler: ICommand?,
                                       longClickHandler: ICommand?) {

        val recyclerView = view as RecyclerView

        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        }

        val adapter = BindingSectionedRecyclerViewAdapter(itemTemplate, itemSource)
        if (clickHandler != null) {
            if(clickHandler is BaseCommandWithContext<*>)
            {
                clickHandler.ctx = view.context
            }
            adapter.setClickHandler(clickHandler)
        }
        if (longClickHandler != null) {
            if(longClickHandler is BaseCommandWithContext<*>)
            {
                longClickHandler.ctx = view.context
            }
            adapter.setLongClickHandler(longClickHandler)
        }
        recyclerView.adapter = adapter
    }


    //endregion

    //endregion

    //region 绑定是否可用，主要用于ViewGroup不可用时其子view都不可用
    @JvmStatic
    @BindingAdapter("enable")
    fun setEnable(view: View, isEnable: Boolean) {
        view.isEnabled = isEnable
        if (view is ViewGroup) {
            var i = 0
            val j = view.childCount
            while (i < j) {
                setEnable(view.getChildAt(i), isEnable)
                i++
            }
        }
    }
    //endregion

    //region EditText
    @JvmStatic
    @BindingAdapter("focusedCursorIndex")
    fun setFocusedSelection(view: EditText, cursorIndex: Int) {
        val originalOnFocusChangeListener = view.onFocusChangeListener
        view.setOnFocusChangeListener { v, hasFocus ->
            originalOnFocusChangeListener?.onFocusChange(v, hasFocus)
            if (hasFocus) {
                if (view.text.isNullOrEmpty()) {
                    return@setOnFocusChangeListener
                }

                val index = if (cursorIndex > view.text.length) view.text.length else cursorIndex
                view.setSelection(index)
                Selection.setSelection(view.text, index)
            }
        }

        //设置的时候可能不触发FocusChange  所以在设置一遍
        if (view.text.isNullOrEmpty()) {
            return
        }
        val index = if (cursorIndex > view.text.length) view.text.length else cursorIndex
        view.setSelection(index)
        Selection.setSelection(view.text, index)
    }

    @JvmStatic
    @BindingAdapter("keyDownCommand")
    fun setOnEditorAction(view: EditText, command: ICommand) {
        view.setOnEditorActionListener { _, actionId, keyEvent ->
            if ((keyEvent?.keyCode ?: KeyEvent.ACTION_UP) == KeyEvent.ACTION_UP) {
                if (actionId == view.imeOptions) {
                    if (command.canExecuteAny(null)) {
                        if(command is BaseCommandWithContext<*>)
                        {
                            command.ctx = view.context
                        }
                        //隐藏键盘
//                        val inputmanger = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                        inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
                        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
                        //执行操作
                        command.executeAny(null)
                    }
                    return@setOnEditorActionListener true
                }
            }
            false
        }
    }
    //endregion

    //region viewPager
    @JvmStatic
    @BindingAdapter(value = *arrayOf("itemSource", "fragmentManager"), requireAll = true)
    fun bindViewPager(view: ViewPager, itemSource: Collection<Fragment>, fragmentManager: FragmentManager) {
        view.adapter = BindingPagerAdapter(fm = fragmentManager, items = itemSource)
    }

    //region 实现双向绑定
    @JvmStatic
    @BindingAdapter(value = "currentPage")
    fun bindViewPagerSetCurrentPage(view: ViewPager, currentPage: Int) {
        if (view.currentItem != currentPage) {
            view.currentItem = currentPage
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "currentPage", event = "currentPageAttrChanged")
    fun bindViewPagerGetCurrentPage(view: ViewPager): Int {
        return view.currentItem
    }

    @JvmStatic
    @BindingAdapter(value = "currentPageAttrChanged")
    fun setCurrentPageAttrChanged(view: ViewPager, inverseBindingListener: InverseBindingListener?) {
        inverseBindingListener?.let { inverseBindingListener ->
            inverseBindingListener.onChange()
            view.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                val bindingListener = java.lang.ref.WeakReference(inverseBindingListener)
                override fun onPageSelected(p0: Int) {
                    val listener = bindingListener.get()
                    listener?.onChange()
                }

                override fun onPageScrollStateChanged(p0: Int) {
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }
            })
        }
    }
    //endregion

    //endregion
}