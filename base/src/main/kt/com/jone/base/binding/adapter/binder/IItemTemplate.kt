package com.jone.base.binding.adapter.binder

import android.databinding.ViewDataBinding

/**
 * Created by Jone.L on 2018/4/12.
 */
interface IItemTemplate<in T> {
    fun getLayoutRes(model: T): Int

    fun getBindingVariable(model: T): Int

    fun isItemCanClick(model: T): Boolean

    fun setVariables(binding: ViewDataBinding, item: T)
}