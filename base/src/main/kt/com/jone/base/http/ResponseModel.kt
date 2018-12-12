package com.jone.base.http

import android.text.TextUtils
import com.jone.base.json.JsonHelper
import com.jone.base.utils.LogUtils
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by Jone.L on 2017/12/4.
 */
open class ResponseModel(jsonStr: String? = null, responseBean: Any? = null) {
    var jsonStr: String = "" //// 返回的原始数据
        internal set(value) {
            field = value
        }
    private var responseBean: Any? = null // 解析出来的对象
    private var jsonObject: JSONObject? = null

    init {
        this.jsonStr = jsonStr ?: ""
        this.responseBean = responseBean
    }

    protected fun initData(responseModel: ResponseModel) {
        this.jsonStr = responseModel.jsonStr
        this.responseBean = responseModel.responseBean
        this.jsonObject = responseModel.jsonObject

    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getResponseBean(): T? {
        return responseBean as T?
    }


    /**
     * 默认不去初始化jsonObject以节约空间
     * 内部方法调用jsonObject也使用该方法；
     *
     * @return
     */
    fun getResponseJsonObject(): JSONObject? {
        if (jsonObject == null) {
            initResponseJsonObject()
        }
        return jsonObject
    }

    private fun initResponseJsonObject() {
        if (jsonObject == null && jsonStr.isNotEmpty()) {
            val Head = jsonStr.substring(0, 1)
            if ("{" == Head) {
                try {
                    jsonObject = JSONObject(jsonStr)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    /**
     * 获取list
     */
    private fun <T> getListObjectInJsonImpl(jsonObject: JSONObject?, keyString: String, cls: Class<T>): List<T>? {
        if (TextUtils.isEmpty(keyString) || jsonObject == null) return null
        return try {
            if (jsonObject.isNull(keyString)) null else JsonHelper.tryConvertJsonToList<T>(jsonObject.getString(keyString), cls)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取list
     *
     * @param keyString
     * @param pKeyString 父节点key
     * @return
     */
    fun <T> getListObjectInJson(keyString: String, cls: Class<T>, vararg pKeyString: String): List<T>? {
        if (TextUtils.isEmpty(keyString) || getResponseJsonObject() == null) return null
        if (pKeyString.isEmpty()) {
            return getListObjectInJsonImpl<T>(getResponseJsonObject(), keyString, cls)
        } else {
            var jsonObject = getResponseJsonObject()
            for (pKey in pKeyString) {
                jsonObject = getPropertyInJsonImpl<JSONObject>(jsonObject, pKey)
                if (jsonObject == null) {
                    return null
                }
            }
            return getListObjectInJsonImpl<T>(jsonObject, keyString, cls)
        }
    }

    /**
     * 获取list
     *
     * @param keyString
     * @param pKeyString 父节点key
     * @return
     */
    inline fun <reified T> getListObjectInJson(keyString: String, vararg pKeyString: String): List<T>? {
        return getListObjectInJson<T>(keyString, T::class.java, *pKeyString)
    }
    //endregion


    //region Object
    /**
     * 获取对象
     */
    private fun <T> getObjectInJsonImpl(jsonObject: JSONObject?, keyString: String, cls: Class<T>): T? {
        if (TextUtils.isEmpty(keyString) || jsonObject == null) return null
        try {
            return if (jsonObject.isNull(keyString)) null else JsonHelper.tryConvertJsonToObject(jsonObject.getString(keyString), cls)
        } catch (e: Exception) {
            LogUtils.e(e)
            return null
        }

    }

    /**
     * 获取对象
     *
     * @param keyString
     * @param pKeyString 父节点key
     * @return
     */
    fun <T> getObjectInJson(keyString: String, cls: Class<T>, vararg pKeyString: String): T? {
        if (TextUtils.isEmpty(keyString) || getResponseJsonObject() == null) return null
        if (pKeyString.isEmpty()) {
            return getObjectInJsonImpl<T>(getResponseJsonObject(), keyString, cls)
        } else {
            var jsonObject = getResponseJsonObject()
            for (pKey in pKeyString) {
                jsonObject = getPropertyInJsonImpl<JSONObject>(jsonObject, pKey)
                if (jsonObject == null) {
                    return null
                }
            }
            return if (jsonObject != null) {
                getObjectInJsonImpl<T>(jsonObject, keyString, cls)
            } else null
        }
    }

    /**
     * 获取对象
     *
     * @param keyString
     * @param pKeyString 父节点key
     * @return
     */
    inline fun <reified T> getObjectInJson(keyString: String, vararg pKeyString: String): T? {
        return getObjectInJson(keyString, T::class.java, *pKeyString)
    }
    //endregion


    //region Property
    /**
     * 根据key单独内容(属性,不是转换为Class)
     */

    private fun <T> getPropertyInJsonImpl(jsonObject: JSONObject?, keyString: String): T? {
        if (keyString.isEmpty() || jsonObject == null) return null
        try {
            @Suppress("UNCHECKED_CAST")
            return if (jsonObject.isNull(keyString)) null else jsonObject.get(keyString) as T?
        } catch (e: Exception) {
            LogUtils.e(e)
            return null
        }

    }

    /**
     * 根据key单独内容(属性,不是转换为Class)
     *
     * @param keyString
     * @param pKeyString 父节点key
     * @return
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getPropertyInJson(keyString: String, vararg pKeyString: String): T? {
        if (keyString.isEmpty() || getResponseJsonObject() == null) return null
        if (pKeyString.isEmpty()) {
            return getPropertyInJsonImpl<T>(getResponseJsonObject(), keyString)
        } else {
            var jsonObject = getResponseJsonObject()
            for (pKey in pKeyString) {
                if (jsonObject?.has(pKey) == true && jsonObject.get(pKey).toString().startsWith("{")) {
                    jsonObject = getPropertyInJsonImpl<JSONObject>(jsonObject, pKey)
                    if (jsonObject == null) {
                        return null
                    }
                }
                else return null
            }
            return if (jsonObject != null) {
                getPropertyInJsonImpl<T>(jsonObject, keyString)
            } else null
        }
    }
    //endregion


    override fun toString(): String {
        return ("ResponeModel [jsonStr=" + jsonStr + ", jsonObject=" + jsonObject + ", responseBean=" + responseBean + "]")
    }
}