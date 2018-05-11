package com.jone.base.impl.http

import com.jone.base.http.IHttpResponseCallback
import com.jone.base.http.RequestParams
import com.jone.base.http.executor.IExecutor
import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Jone on 2018/4/16.
 */
class OkHttpExecutor(config: OkhttpConfig) : IExecutor {
    //配置okhttpclient，全局唯一
    val mClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder().retryOnConnectionFailure(config.retryOnConnectionFailure).connectTimeout(config.connectTimeout, TimeUnit.MILLISECONDS).readTimeout(config.readTimeout,
                TimeUnit.MILLISECONDS).writeTimeout(config.writeTimeout, TimeUnit.MILLISECONDS)
        if (config.cache != null) {
            builder.cache(config.cache)
        }

        if (config.x509TrustManager != null && config.sslSocketFactory != null) {
            builder.sslSocketFactory(config.sslSocketFactory!!, config.x509TrustManager!!)
        }
        if (config.interceptors.isNotEmpty()) {
            config.interceptors.forEach { item -> builder.addInterceptor(item) }
        }

        if (config.networkInterceptors.isNotEmpty()) {
            config.networkInterceptors.forEach { item -> builder.addNetworkInterceptor(item) }
        }
        builder.build()
    }

    val calls: MutableMap<Any, ArrayList<Call>> = HashMap()

    override fun doGet(requestParams: RequestParams, callback: IHttpResponseCallback?) {
        callback?.onPreExecute(requestParams)
        val request = Request.Builder()
                .url(OkhttpUtils.getUrlWithParams(requestParams))
                .get()
                .headers(OkhttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag)
                .build()
        val okHttpCall = mClient.newCall(request)
        okHttpCall.enqueue(OkHttpCallbackHandler(callback))
        cacheCall(requestParams, okHttpCall)
    }

    override fun doPost(requestParams: RequestParams, callback: IHttpResponseCallback?) {
        callback?.onPreExecute(requestParams)
        val request = Request.Builder()
                .url(requestParams.url)
                .post(OkhttpUtils.getPostBody(requestParams))
                .headers(OkhttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag).build()
        val okHttpCall = mClient.newCall(request)
        okHttpCall.enqueue(OkHttpCallbackHandler(callback))
        cacheCall(requestParams, okHttpCall)
    }

    override fun doPostJson(requestParams: RequestParams, callback: IHttpResponseCallback?) {
        callback?.onPreExecute(requestParams)
        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestParams.body!!)
        val request = Request.Builder().url(requestParams.url)
                .post(requestBody)
                .headers(OkhttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag)
                .build()
        val okHttpCall = mClient.newCall(request)
        okHttpCall.enqueue(OkHttpCallbackHandler(callback))
        cacheCall(requestParams, okHttpCall)
    }

    override fun doUploadFile(requestParams: RequestParams, callback: IHttpResponseCallback?) {
        callback?.onPreExecute(requestParams)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestParams.files.forEach{(itemKey,itemValue)-> builder.addFormDataPart(itemKey, itemValue.name, RequestBody.create(MediaType.parse("image/png"), itemValue))}
        val progressRequestBody = ProgressRequestBody(builder.build())
        val request = Request.Builder().
                url(requestParams.url)
                .post(progressRequestBody)
                .headers(OkhttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag).build()
        val okHttpCall = mClient.newCall(request)
        okHttpCall.enqueue(OkHttpCallbackHandler(callback))
        cacheCall(requestParams, okHttpCall)
    }

    override fun doDownLoad(requestParams: RequestParams, callback: IHttpResponseCallback?) {
        callback?.onPreExecute(requestParams)
        val request = Request.Builder()
                .url(OkhttpUtils.getUrlWithParams(requestParams))
                .get()
                .headers(OkhttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag).build()
        val okHttpCall = mClient.newCall(request)
        okHttpCall.enqueue(OkHttpDownloadCallbackHandler(requestParams, callback))
        cacheCall(requestParams, okHttpCall)
    }

    override fun cancel(tag: Any) {
        calls[tag]?.forEach { item ->
            when (item.isCanceled) {false -> {
                item.cancel()
                calls[tag]!!.remove(item)
            }
            }
        }
    }

    override fun cancelAll() {
        calls.forEach{(tag,_)->cancel(tag)}
    }

    fun cacheCall(requestParams: RequestParams, call: Call) {
        if (requestParams.tag == null) {
            return
        }
        var values: ArrayList<Call>? = calls[requestParams.tag!!]
        if (values == null) {
            values = ArrayList()
            values.add(call)
            calls.put(requestParams.tag!!, values)
        } else {
            values.add(call)
        }
    }
}