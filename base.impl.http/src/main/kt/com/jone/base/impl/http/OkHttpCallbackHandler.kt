package com.jone.base.impl.http

import android.os.AsyncTask
import com.jone.base.http.IHttpResponseCallback
import com.jone.base.http.ResponseModel
import com.jone.base.utils.extend.log

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.util.regex.Pattern
import com.jone.base.utils.LogUtils
import com.jone.base.json.JsonHelper


/**
 * Created by Jone on 2018/4/16.
 */
class OkHttpCallbackHandler(val responseCallback: IHttpResponseCallback?) : Callback {
    override fun onFailure(call: Call, e: IOException) {
        e.log()
        responseCallback?.onError(e)
    }

    override fun onResponse(call: Call, response: Response) {

            responseCallback?.let {
                HandleJsonAsync(responseCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response)
            }
    }

    private class HandleJsonAsync(val responseCallback: IHttpResponseCallback) : AsyncTask<Response, Void, HandledResponseModel>() {
        override fun doInBackground(vararg params: Response): HandledResponseModel {
            val responseJsonStr = params[0].body()?.string()
            if (responseCallback.getResponseModelType()==null|| responseCallback.getResponseModelType()!!.name == String::class.java.name||responseJsonStr.isNullOrEmpty()) {
                return  HandledResponseModel( ResponseModel(responseJsonStr));
            }
            //去除json体前面的制表符、回车符\空格

            val responseJsonStrNew = if (responseJsonStr!!.startsWith("\\s") || responseJsonStr.startsWith("\n") || responseJsonStr.startsWith("\t") || responseJsonStr.startsWith("\r"))
            {
                Pattern.compile("\\s*|\t*|\r*|\n*|\r\n*").matcher(responseJsonStr).replaceFirst("")
            }else {
                responseJsonStr
            }
            val Head = responseJsonStrNew.substring(0, 1)
            try {
                var responseBean: Any? = null
                if ("{" == Head) { // 是对象
                    responseBean = JsonHelper.convertJsonToObject(responseJsonStrNew, responseCallback.getResponseModelType()!!)
                } else if ("[" == Head) {//是数组
                    responseBean = if (responseCallback.getResponseModelType()!!.isAssignableFrom(List::class.java))
                        JsonHelper.convertJsonToObject(responseJsonStr, responseCallback.getResponseModelType()!!)
                    else
                        JsonHelper.convertJsonToList(responseJsonStr, responseCallback.getResponseModelType()!!)

                }
                if (responseBean == null) {//不是解析失败的null bean让其实例化，省得调用的地方先判断bean不为空在判断msg
                    responseBean = responseCallback.getResponseModelType()!!.newInstance()
                }
                return HandledResponseModel(ResponseModel(responseJsonStr, responseBean))

            } catch (e: Exception) {
                LogUtils.e(e)

                return HandledResponseModel(e=e)
            }
        }

        override fun onPostExecute(result: HandledResponseModel) {
            super.onPostExecute(result)
            result.responseModel?.apply { responseCallback.onSuccess(result.responseModel) }?:responseCallback.onError(result.e!!)
        }
    }

    private class HandledResponseModel(val responseModel:ResponseModel?=null,val e:Exception?=null)
}