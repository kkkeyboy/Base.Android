package com.jone.base.binding.command

interface ICommandExecuteCallback<T> {
    fun onExecuted(commandParameter:T)
}