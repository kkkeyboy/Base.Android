package com.jone.base.json;

import com.jone.base.utils.extend.log

/**
 * Created by Jone.L on 2017/3/11.
 * Json工具类，确保在使用调用了setJsonUtilImpl进行初始化json实现类
 */

object JsonHelper : JsonHelperProtocol
{
    fun <T> tryConvertJsonToList(json: String, clazz: Class<T>): List<T>?
    {
        return try
        {
            convertJsonToList(json, clazz)
        }
        catch (e: Exception)
        {
            e.log("JsonHelper $e")
            null
        }
    }

    fun <T> tryConvertJsonToObject(json: String, clazz: Class<T>): T?
    {
        return try
        {
            convertJsonToObject(json, clazz)
        }
        catch (e: Exception)
        {
            e.log("JsonHelper $e")
            null
        }
    }

    fun tryConvertObjectToJson(obj: Any): String?
    {
        return try
        {
            convertObjectToJson(obj)
        }
        catch (e: Exception)
        {
            e.log("JsonHelper $e")
            null
        }
    }


    override fun <T> convertJsonToList(json: String, clazz: Class<T>): List<T>?
    {
        return JsonHelperStrategy.jsonHelperImpl!!.convertJsonToList(json, clazz)
    }

    override fun <T> convertJsonToObject(json: String, clazz: Class<T>): T?
    {
        return JsonHelperStrategy.jsonHelperImpl!!.convertJsonToObject(json, clazz)
    }

    override fun convertObjectToJson(obj: Any): String?
    {
        return JsonHelperStrategy.jsonHelperImpl!!.convertObjectToJson(obj)
    }
}
