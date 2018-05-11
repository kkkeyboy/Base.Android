package com.jone.base.cache.image

import android.content.Context
import android.widget.ImageView

/**
 * Created by Jone.L on 2017/11/30.
 */
interface ImageHelperProtocol
{

    fun loadImage(url: String,
                  imageView: ImageView,
                  context: Context = imageView.context,
                  placeholder: Int = 0,
                  placeholderError: Int = 0,
                  listenerProgress: ProgressLoadListener? = null,
                  listenerSourceReady: SourceReadyListener? = null)

    fun loadCircleImage(url: String,
                        imageView: ImageView,
                        context: Context = imageView.context,
                        placeholder: Int = 0,
                        placeholderError: Int = 0,
                        borderThickness: Float = 0.toFloat(),
                        borderColor: Int = 0,
                        listenerProgress: ProgressLoadListener? = null,
                        listenerSourceReady: SourceReadyListener? = null)


    fun loadGifImage(url: String,
                     imageView: ImageView,
                     context: Context = imageView.context,
                     placeholder: Int = 0,
                     placeholderError: Int = 0,
                     listenerProgress: ProgressLoadListener? = null,
                     listenerSourceReady: SourceReadyListener? = null)

    //清除硬盘缓存
    fun clearImageDiskCache(context: Context)

    //清除内存缓存
    fun clearImageMemoryCache(context: Context)

    //根据不同的内存状态，来响应不同的内存释放策略
    fun trimMemory(context: Context, level: Int)

    //获取缓存大小
    fun getCacheSize(context: Context): String

    fun saveImage(context: Context, url: String, savePath: String, saveFileName: String, listener: ImageSaveListener)
}