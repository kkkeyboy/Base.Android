package com.jone.base.binding.command

/**
 * Created by Jone.L on 2017/3/9.
 * 绑定事件（命令/监听器）接口
 */

import android.databinding.Bindable
import android.databinding.Observable

interface ICommand: Observable{
    @get:Bindable
    var isEnable: Boolean

    @get:Bindable
    var isRefreshing: Boolean

    /**
     * 具体回调
     */
    fun executeAny(commandParameter: Any?)

    /**
     * 检查是否能执行
     */
    fun canExecuteAny(commandParameter: Any?): Boolean
}