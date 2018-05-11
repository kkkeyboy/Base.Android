package com.jone.base.binding.adapter.binder

import android.databinding.ViewDataBinding

/**
 * Created by Jone.L on 2018/4/12.
 */
class ItemTemplateCollection<in T>(vararg val conditionalDataBinders: ItemTemplate<T>) : IItemTemplate<T> {

    override fun getLayoutRes(model: T) =
            conditionalDataBinders.first { item -> item.canHandle(model) }.getLayoutRes(model)

    override fun getBindingVariable(model: T) =
            conditionalDataBinders.first { item -> item.canHandle(model) }.getBindingVariable(model)

    override fun isItemCanClick(model: T) =
            conditionalDataBinders.first { item -> item.canHandle(model) }.isItemCanClick(model)

    override fun setVariables(binding: ViewDataBinding, item: T) {
        conditionalDataBinders.first { it->it.canHandle(item) }.setVariables(binding,item)
    }

}