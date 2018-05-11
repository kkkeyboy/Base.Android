package com.jone.base.impl.http

import com.jone.base.http.IHttpResponseCallback
import com.jone.base.http.ResponseModel
import com.jone.base.utils.extend.log

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jone on 2018/4/16.
 */
class OkHttpCallbackHandler(val responseCallback: IHttpResponseCallback?) : Callback {
    override fun onFailure(call: Call, e: IOException) {
        e.log()
        responseCallback?.onError(e)
    }

    override fun onResponse(call: Call, response: Response) {
        responseCallback?.onSuccess(ResponseModel(jsonStr = response.body()?.string()))
    }
}