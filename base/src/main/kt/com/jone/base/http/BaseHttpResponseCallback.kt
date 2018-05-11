package com.jone.base.http

import java.lang.reflect.ParameterizedType


/**
 * Created by Jone.L on 2018/4/18.
 */
abstract class BaseHttpResponseCallback<T>:ResponseModel() ,IHttpResponseCallback  {
    private var mResponseModelType:Class<T>?=null

    final override fun onSuccess(responseModel: ResponseModel) {
        initData(responseModel)
        onSuccess()
    }

    @Suppress("UNCHECKED_CAST")
    final override  fun getResponseModelType(): Class<*> {
        if (mResponseModelType == null) {
            mResponseModelType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        }
        return mResponseModelType!!
    }

    abstract fun onSuccess()

}