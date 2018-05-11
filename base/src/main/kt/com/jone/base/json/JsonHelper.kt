package com.jone.base.json;

import com.jone.base.utils.extend.log

/**
 * Created by Jone.L on 2017/3/11.
 * Json工具类，确保在使用调用了setJsonUtilImpl进行初始化json实现类
 */

object JsonHelper : JsonHelperProtocol {
    override fun <T> convertJsonToList(json: String, clazz: Class<T>): List<T>? {
        return try {
            JsonHelperStrategy.jsonHelperImpl!!.convertJsonToList(json, clazz)
        } catch (e: Exception) {
            e.log("JsonHelper")
            null
        }
    }

    override fun <T> convertJsonToObject(json: String, clazz: Class<T>): T? {
        return try {
            JsonHelperStrategy.jsonHelperImpl!!.convertJsonToObject(json, clazz)
        } catch (e: Exception) {
            e.log("JsonHelper")
            null
        }
    }

    override fun convertObjectToJson(obj: Any): String? {
        return try {
            JsonHelperStrategy.jsonHelperImpl!!.convertObjectToJson(obj)
        } catch (e: Exception) {
            e.log("JsonHelper")
            null
        }
    }
}
