package com.jone.base.http

import java.lang.reflect.ParameterizedType


/**
 * Created by Jone.L on 2018/4/18.
 */
abstract class BaseHttpResponseCallback<T> : ResponseModel(), IHttpResponseCallback {
    private var mResponseModelType: Class<T>? = null

    final override fun onSuccess(responseModel: ResponseModel) {
        initData(responseModel)
        onSuccess()
    }

    @Suppress("UNCHECKED_CAST")
    final override fun getResponseModelType(): Class<T>? {
        if (mResponseModelType == null) {
            mResponseModelType = getOverrideResponseModelType() ?: (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        }
        return mResponseModelType!!
    }

    protected open fun getOverrideResponseModelType(): Class<T>? = null

    abstract fun onSuccess()

}