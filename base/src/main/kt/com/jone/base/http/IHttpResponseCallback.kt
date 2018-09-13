package com.jone.base.http

/**
 * Created by Jone.L on 2017/12/4.
 */
interface IHttpResponseCallback {

    val isSuccess: Boolean

    fun onPreExecute(params: RequestParams)

    fun onSuccess(responseModel: ResponseModel)

    fun onError(e: Exception)

    fun onFinish()

    fun onProgress(progress: Float)

    fun getResponseModelType(): Class<*>?
}