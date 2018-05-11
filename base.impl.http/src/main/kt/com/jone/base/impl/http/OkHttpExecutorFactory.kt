package com.jone.base.impl.http

import com.jone.base.http.executor.IExecutor
import com.jone.base.http.executor.IExecutorFactory
import com.jone.base.utils.extend.logD

/**
 * Created by Jone on 2018/4/17.
 */
class OkHttpExecutorFactory : IExecutorFactory
{
    override fun create(): IExecutor
    {

        val logInterceptor = LoggingInterceptor(ConsoleLogger())
        logInterceptor.setLevel(LoggingInterceptor.Level.BODY)

        //建造者模式，生成配置对象
        val config = OkhttpConfig.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(10000).readTimeout(5000)
                .writeTimeout(5000)
                .interceptors(logInterceptor)
                .build()

        return OkHttpExecutor(config)
    }

   internal class ConsoleLogger: LoggingInterceptor.Logger {
        override fun log(message: String) {
            message.logD("http")
        }
    }
}