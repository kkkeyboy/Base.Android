package com.jone.base.cache.image

/**
 * Created by Jone.L on 2017/11/30.
 */
object ImageHelperStrategy
{
    internal var imageHelperImpl: ImageHelperProtocol? = null

    fun seImageHelperImpl(imageHelperImpl: ImageHelperProtocol)
    {
        this.imageHelperImpl = imageHelperImpl
    }
}