package com.jone.base.json

/**
 * Created by Jone.L on 2018/3/27.
 *  Json工具类协议
 */
interface JsonHelperProtocol
{
    fun <T> convertJsonToList(json: String, clazz: Class<T>): List<T>?

    infix fun <reified T> convertJsonToObject(json: String): T?

    fun convertObjectToJson(obj: Any): String?

}