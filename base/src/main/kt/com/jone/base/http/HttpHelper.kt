package com.jone.base.http

/**
 * Created by Jone.L on 2018/4/13.
 */
object HttpHelper {

    fun get(url: String) = RequestParams.Builder(RequestParams.Method.GET, url, HttpRequestStrategy.executorImpl!!)

    fun post(url: String) = RequestParams.Builder(RequestParams.Method.POST, url, HttpRequestStrategy.executorImpl!!)

    fun postJson(url: String) = RequestParams.Builder(RequestParams.Method.POST_JSON, url, HttpRequestStrategy.executorImpl!!)

    fun upload(url: String) = RequestParams.Builder(RequestParams.Method.UPLOAD, url, HttpRequestStrategy.executorImpl!!)

    fun download(url: String) = RequestParams.Builder(RequestParams.Method.DOWNLOAD, url, HttpRequestStrategy.executorImpl!!)

    /**
     * 取消某个请求,一般在页面销毁的时候取消
     *
     * @param tag
     */
    fun cancelRequest(tag: Any) {
        HttpRequestStrategy.executorImpl!!.cancel(tag)
    }

    /**
     * 取消所有的请求
     */
    fun cancelAllRequest() {
        HttpRequestStrategy.executorImpl!!.cancelAll()
    }
}