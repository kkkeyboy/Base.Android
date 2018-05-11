package com.jone.base.http.executor

/**
 * Created by Jone.L on 2017/12/4.
 */
interface IExecutorFactory
{
    /**
     * 具体请求工厂类，扩展请继承
     * @return
     */
    fun create(): IExecutor
}