package com.jone.base.impl.http

import com.jone.base.http.RequestParams
import okhttp3.FormBody
import okhttp3.Headers

/**
 * Created by Jone on 2018/4/16.
 */
object OkhttpUtils {
    fun getUrlWithParams(requestParams: RequestParams): String {
        val url = requestParams.url
        val params = requestParams.requestParams
        if (params.isEmpty()) {
            return url
        }

        val sb = StringBuffer()
        params.forEach { (itemKey, itemValue) -> sb.append(itemKey + "=").append(itemValue + "&") }.let { sb.deleteCharAt(sb.length - 1) }
        return if (url.endsWith("?")) "${url}${sb.toString()}" else "${url}?${sb.toString()}"
    }

    fun getPostBody(requestParams: RequestParams): FormBody {
        val bodyBuilder = FormBody.Builder()
        val params = requestParams.requestParams
        if (params.isEmpty()) {
            return bodyBuilder.build()
        }

        params.forEach { (itemKey, itemValue) -> bodyBuilder.addEncoded(itemKey,itemValue) }.let { return bodyBuilder.build() }
    }

    fun getHeaders(requestParams: RequestParams): Headers {
        val builder = Headers.Builder()
        val params = requestParams.header
        if (params.isEmpty()) {
            return builder.build()
        }

        params.forEach { (itemKey, itemValue) -> builder.add(itemKey,itemValue) }.let { return builder.build() }
    }
}