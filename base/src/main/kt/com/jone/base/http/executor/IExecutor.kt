package com.jone.base.http.executor

import com.jone.base.http.IHttpResponseCallback
import com.jone.base.http.RequestParams

/**
 * Created by Jone.L on 2017/12/4.
 */
interface IExecutor
{
    /**
     * get请求
     */
    fun doGet(requestParams: RequestParams, callback: IHttpResponseCallback?=null)

    /**
     * post请求 key-value
     */
    fun doPost(requestParams: RequestParams, callback: IHttpResponseCallback?=null)

    /**
     * post请求 json
     */
    fun doPostJson(requestParams: RequestParams, callback: IHttpResponseCallback?=null)

    /**
     * uploadFile
     */
    fun doUploadFile(requestParams: RequestParams, callback: IHttpResponseCallback?=null)

    /**
     * downLoad
     */
    fun doDownLoad(requestParams: RequestParams, callback: IHttpResponseCallback?=null)

    /**
     * 取消请求
     */
    fun cancel(tag: Any)

    /**
     * 取消所有请求
     */
    fun cancelAll()
}