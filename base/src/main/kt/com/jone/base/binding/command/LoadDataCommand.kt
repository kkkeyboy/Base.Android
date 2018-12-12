package com.jone.base.binding.command

import android.databinding.Bindable

import com.jone.base.BR

/**
 * Created by Jone.L on 2017/4/9.
 * 加载数据command
 */

abstract class LoadDataCommand : BaseCommand<LoadDataCommand.ILoadingCallback>() {
    companion object
    {
        fun  commandWithLoadData(canExecuteBlock: LoadDataCommand.(commandParameter: LoadDataCommand.ILoadingCallback) -> Boolean = { _ -> BaseCommand@ this.isEnable && !BaseCommand@ this.isRefreshing }, executeBlock: LoadDataCommand.(commandParameter: LoadDataCommand.ILoadingCallback) -> Unit): LoadDataCommand {
            return object : LoadDataCommand() {
                override fun execute(commandParameter: LoadDataCommand.ILoadingCallback) {
                    executeBlock.invoke(this, commandParameter)
                }

                override fun canExecute(commandParameter: LoadDataCommand.ILoadingCallback): Boolean {
                    return canExecuteBlock.invoke(this, commandParameter)
                }
            }
        }
    }

    //加载更多模式
    @get:Bindable
    var isLoadMore: Boolean = false
    set(value) {
        if(field!=value)
        {
            field = value
            notifyPropertyChanged(BR.loadMore)
        }
    }

    override fun canExecute(commandParameter: ILoadingCallback): Boolean {
        return true
    }

    interface ILoadingCallback {

        fun onRefreshComplete()

        fun onLoadMoreComplete()

        fun setNoMore(value: Boolean)

    }

    abstract class BaseLoadingCallback : ILoadingCallback {
        override fun onLoadMoreComplete() {

        }

        override fun setNoMore(value: Boolean) {
        }
    }

}