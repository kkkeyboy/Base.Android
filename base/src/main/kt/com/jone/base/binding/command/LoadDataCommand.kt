package com.jone.base.binding.command

import android.databinding.Bindable

import com.jone.base.BR

/**
 * Created by Jone.L on 2017/4/9.
 * 加载数据command
 */

abstract class LoadDataCommand : BaseCommand<LoadDataCommand.ILoadingCallback>() {
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