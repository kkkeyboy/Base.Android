package com.jone.base.binding.adapter.binder

import android.databinding.ViewDataBinding

/**
 * Created by Jone.L on 2018/4/12.
 */
open class ItemTemplateBase<in T>(private val bindingVariable: Int, private val layoutId: Int) : IItemTemplate<T> {

    override fun getLayoutRes(model: T): Int {
        return layoutId
    }

    override fun getBindingVariable(model: T): Int {
        return bindingVariable
    }

    override fun isItemCanClick(model: T): Boolean {
        return true
    }

    override fun setVariables(binding: ViewDataBinding, item: T) {}

}