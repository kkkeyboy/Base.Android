package com.jone.base.utils.extend

import android.content.Context
import android.content.pm.ApplicationInfo


/**
 * 检测是否处于debug模式。
 */
fun Context.isDebug(): Boolean
{
    return try
    {
        this.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }
    catch (e: Exception)
    {
        false
    }

}
