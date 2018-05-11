package com.jone.base.json

/**
 * Created by Jone.L on 2018/3/27.
 *  Json工具类协议
 */
interface JsonHelperProtocol {
    fun <T> convertJsonToList(json: String, clazz: Class<T>): List<T>?

    fun <T> convertJsonToObject(json: String, clazz: Class<T>): T?

    fun convertObjectToJson(obj: Any): String?

}