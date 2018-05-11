package com.jone.base.cache.image

/**
 * Created by Jone.L on 2017/11/30.
 */
interface ImageSaveListener
{
    fun onSaveSuccess()
    fun onSaveFail()
}

interface SourceReadyListener
{
    fun onResourceReady(width: Int, height: Int)
}

interface ProgressLoadListener
{
    fun update(bytesRead: Int, contentLength: Int)
    fun onException()
    fun onResourceReady()
}