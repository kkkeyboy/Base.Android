package com.jone.base.utils.extend

import com.jone.base.binding.command.BaseCommand
import com.jone.base.binding.command.BaseCommandWithContext
import com.jone.base.binding.command.LoadDataCommand

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

fun  commandWithLoadData(canExecuteBlock: LoadDataCommand.(commandParameter: LoadDataCommand.ILoadingCallback) -> Boolean = { _ -> true }, executeBlock: LoadDataCommand.(commandParameter: LoadDataCommand.ILoadingCallback) -> Unit): LoadDataCommand {
    return object : LoadDataCommand() {
        override fun execute(commandParameter: LoadDataCommand.ILoadingCallback) {
            executeBlock.invoke(this, commandParameter)
        }

        override fun canExecute(commandParameter: LoadDataCommand.ILoadingCallback): Boolean {
            return canExecuteBlock.invoke(this, commandParameter)
        }
    }
}