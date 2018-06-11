package com.jone.base.binding.command

/**
 * Created by Jone.L on 2017/3/9.
 * 绑定命令基类
 */

import android.databinding.BaseObservable
import android.databinding.Bindable

import com.jone.base.BR


abstract class BaseCommand<in T : Any?> : BaseObservable(), ICommand {
    companion object
    {
        fun <T> command(canExecuteBlock: BaseCommand<T>.(commandParameter: T) -> Boolean = { _ -> BaseCommand@ this.isEnable && !BaseCommand@ this.isRefreshing }, executeBlock: BaseCommand<T>.(commandParameter: T) -> Unit): BaseCommand<T> {
            return object : BaseCommand<T>() {
                override fun execute(commandParameter: T) {
                    executeBlock.invoke(this, commandParameter)
                }

                override fun canExecute(commandParameter: T): Boolean {
                    return canExecuteBlock.invoke(this, commandParameter)
                }
            }
        }

        fun <T> commandWithContext(canExecuteBlock: BaseCommand<T>.(commandParameter: T) -> Boolean = { _ -> BaseCommandWithContext@ this.isEnable && !BaseCommandWithContext@ this.isRefreshing }, executeBlock: BaseCommandWithContext<T>.(commandParameter: T) -> Unit): BaseCommandWithContext<T> {
            return object : BaseCommandWithContext<T>() {
                override fun execute(commandParameter: T) {
                    executeBlock.invoke(this, commandParameter)
                }

                override fun canExecute(commandParameter: T): Boolean {
                    return canExecuteBlock.invoke(this, commandParameter)
                }
            }
        }
    }
    override var isEnable: Boolean = true
        set(value: Boolean) {
            field = value
            notifyPropertyChanged(BR.enable)
        }

    override var isRefreshing: Boolean = false
        set(value: Boolean) {
            field = value
            notifyPropertyChanged(BR.refreshing)
        }

    @Suppress("UNCHECKED_CAST")
    final override fun canExecuteAny(commandParameter: Any?) = canExecute(commandParameter as T)

    @Suppress("UNCHECKED_CAST")
    final override fun executeAny(commandParameter: Any?) {
        execute(commandParameter as T)
    }

    open fun canExecute(commandParameter: T) = this.isEnable && !this.isRefreshing
    abstract fun execute(commandParameter: T)

}
