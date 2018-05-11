package com.jone.base.cache.image

import android.content.Context
import android.widget.ImageView


/**
 * Created by Jone.L on 2017/11/30.
 */
object ImageHelper
{
    internal var customImageHelperConfig: ImageHelperConfig? = null;


    @JvmOverloads
    fun loadImage(url: String,
                  imageView: ImageView,
                  context: Context = imageView.context.applicationContext,
                  placeholder: Int? = customImageHelperConfig?.placeHolder ?: 0,
                  placeholderError: Int? = customImageHelperConfig?.placeHolderError ?: 0,
                  listenerProgress: ProgressLoadListener? = null,
                  listenerSourceReady: SourceReadyListener? = null)
    {
        ImageHelperStrategy.imageHelperImpl!!.loadImage(url, imageView, context, placeholder?:customImageHelperConfig?.placeHolder ?: 0, placeholderError?:customImageHelperConfig?.placeHolderError ?: 0, listenerProgress, listenerSourceReady)
    }

    @JvmOverloads
    fun loadCircleImage(url: String,
                        imageView: ImageView,
                        context: Context = imageView.context.applicationContext,
                        placeholder: Int = customImageHelperConfig?.placeHolder ?: 0,
                        placeholderError: Int = customImageHelperConfig?.placeHolderError ?: 0,
                        borderThickness: Float = 0.toFloat(),
                        borderColor: Int = 0,
                        listenerProgress: ProgressLoadListener? = null,
                        listenerSourceReady: SourceReadyListener? = null)
    {
        ImageHelperStrategy.imageHelperImpl!!.loadCircleImage(url, imageView, context, placeholder, placeholderError, borderThickness, borderColor, listenerProgress, listenerSourceReady)
    }

    @JvmOverloads
    fun loadGifImage(url: String,
                     imageView: ImageView,
                     context: Context = imageView.context.applicationContext,
                     placeholder: Int = customImageHelperConfig?.placeHolder ?: 0,
                     placeholderError: Int = customImageHelperConfig?.placeHolderError ?: 0,
                     listenerProgress: ProgressLoadListener? = null,
                     listenerSourceReady: SourceReadyListener? = null)
    {
        ImageHelperStrategy.imageHelperImpl!!.loadGifImage(url, imageView, context, placeholder, placeholderError, listenerProgress, listenerSourceReady)
    }


    /**
     * 需要展示图片加载进度的请参考 GalleryAdapter
     * 样例如下所示
     */

    /**
     * 清除图片磁盘缓存
     */
    fun clearImageDiskCache(context: Context)
    {
        ImageHelperStrategy.imageHelperImpl!!.clearImageDiskCache(context)
    }

    /**
     * 清除图片内存缓存
     */
    fun clearImageMemoryCache(context: Context)
    {
        ImageHelperStrategy.imageHelperImpl!!.clearImageMemoryCache(context)
    }

    /**
     * 根据不同的内存状态，来响应不同的内存释放策略
     *
     * @param context
     * @param level
     */
    fun trimMemory(context: Context, level: Int)
    {
        ImageHelperStrategy.imageHelperImpl!!.trimMemory(context, level)
    }

    /**
     * 清除图片所有缓存
     */
    fun clearImageAllCache(context: Context)
    {
        clearImageDiskCache(context.applicationContext)
        clearImageMemoryCache(context.applicationContext)
    }

    /**
     * 获取缓存大小
     *
     * @return CacheSize
     */
    fun getCacheSize(context: Context): String?
    {
        return ImageHelperStrategy.imageHelperImpl!!.getCacheSize(context)
    }

    fun saveImage(context: Context, url: String, savePath: String, saveFileName: String, listener: ImageSaveListener)
    {
        ImageHelperStrategy.imageHelperImpl!!.saveImage(context, url, savePath, saveFileName, listener)
    }
}