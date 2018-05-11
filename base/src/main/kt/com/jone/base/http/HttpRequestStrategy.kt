package com.jone.base.http

import com.jone.base.http.executor.IExecutor

/**
 * Created by Jone.L on 2018/4/13.
 */
object HttpRequestStrategy {

    internal var executorImpl: IExecutor? = null

    fun seHttpExecutorImpl(executorImpl: IExecutor)
    {
        this.executorImpl = executorImpl
    }
}