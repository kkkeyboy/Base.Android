package com.jone.base.impl.json

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * Created by Jone on 2018/4/16.
 */
class GsonImpl : com.jone.base.json.JsonHelperProtocol {
    private val mGson by lazy {
        GsonBuilder().setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create()
    }

    override fun <T> convertJsonToList(json: String, clazz: Class<T>): List<T> {
        val type = object : TypeToken<ArrayList<T>>() {}.type
        return mGson!!.fromJson<List<T>>(json, type)
    }

    override fun <T> convertJsonToObject(json: String, clazz: Class<T>): T {
        return mGson!!.fromJson(json, clazz)
    }

    override fun convertObjectToJson(obj: Any): String {
        return mGson!!.toJson(obj)
    }

}