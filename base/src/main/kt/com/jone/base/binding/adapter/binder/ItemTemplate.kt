package com.jone.base.binding.adapter.binder

import android.support.annotation.LayoutRes

/**
 * Created by Jone.L on 2018/4/12.
 */
open class ItemTemplate<in T>(bindingVariable: Int, @LayoutRes layoutId: Int) : ItemTemplateBase<T>(bindingVariable, layoutId) {

    /**
     * 多种样式的时候重写该方法进行多个Template的选择
     *
     * @param model
     * @return
     */
   open fun canHandle(model: T?) = model != null

}