package com.jone.base.json

/**
 * Created by Jone.L on 2017/11/25.
 */
object JsonHelperStrategy
{
    internal var jsonHelperImpl: JsonHelperProtocol? = null

    fun setJsonHelperImpl(jsonHelperImpl: JsonHelperProtocol)
    {
        this.jsonHelperImpl = jsonHelperImpl
    }
}