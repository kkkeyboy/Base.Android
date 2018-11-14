package com.jone.base.utils

import android.content.Context
import android.util.Log
import com.jone.base.utils.extend.decodeUnicode
import com.jone.base.utils.extend.isDebug

/**
 * Created by Jone on 2017/12/2.
 */
object LogUtils
{
    private var isLog = true
    private var defaultTag = "UuU"

    @JvmOverloads
    @JvmStatic
    fun init(context: Context, tag: String = defaultTag, isLog: Boolean = context.isDebug())
    {
        this.isLog = isLog
        this.defaultTag = tag
    }


    @JvmOverloads
    @JvmStatic
    fun e(vararg logs: Any, tag: String = defaultTag)
    {
        if (isLog)
        {
            val sbLog = StringBuilder()
            for (log in logs)
            {
                if (log is Exception)
                {
                    sbLog.append(log.message)
                    sbLog.append("\r\n")
                    for (element in log.stackTrace)
                    {
                        sbLog.append(element.toString())
                        sbLog.append("\r\n")
                    }
                }
                else
                {
                    sbLog.append(log.toString().decodeUnicode())
                }
                Log.e(tag, sbLog.toString())
            }
        }
    }

    @JvmOverloads
    @JvmStatic
    fun i(log: Any, tag: String = defaultTag)
    {
        if (isLog)
        {
            Log.i(tag, log.toString().decodeUnicode())
        }
    }


    @JvmOverloads
    @JvmStatic
    fun w(log: Any, tag: String = defaultTag)
    {
        if (isLog)
        {
            Log.w(tag, log.toString().decodeUnicode())
        }
    }

    @JvmOverloads
    @JvmStatic
    fun d(log: Any, tag: String = defaultTag)
    {
        if (isLog)
        {
            Log.d(tag, log.toString().decodeUnicode())
        }
    }


    @JvmOverloads
    @JvmStatic
    fun v(log: Any, tag: String = defaultTag)
    {
        if (isLog)
        {
            Log.v(tag, log.toString().decodeUnicode())
        }
    }


    @JvmOverloads
    @JvmStatic
    fun wtf(log: Any, tag: String = defaultTag)
    {
        if (isLog)
        {
            Log.wtf(tag, log.toString().decodeUnicode())
        }
    }

}

