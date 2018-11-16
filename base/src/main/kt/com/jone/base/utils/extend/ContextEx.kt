package com.jone.base.utils.extend

import android.app.Activity
import android.app.Fragment
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.jone.base.utils.ContextUtils


//region activity相关操作

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
        ContextUtils.startActivity(this, T::class.java, params)

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) =
        ContextUtils.startActivity(activity, T::class.java, params)

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        ContextUtils.startActivityForResult(this, T::class.java, requestCode, params)

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        startActivityForResult(ContextUtils.createIntent(activity, T::class.java, params), requestCode)

inline fun <reified T : Service> Context.startService(vararg params: Pair<String, Any?>) =
        ContextUtils.startService(this, T::class.java, params)

inline fun <reified T : Service> Fragment.startService(vararg params: Pair<String, Any?>) =
        ContextUtils.startService(activity, T::class.java, params)

inline fun <reified T : Service> Context.stopService(vararg params: Pair<String, Any?>) =
        ContextUtils.stopService(this, T::class.java, params)

inline fun <reified T : Service> Fragment.stopService(vararg params: Pair<String, Any?>) =
        ContextUtils.stopService(activity, T::class.java, params)

inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
        ContextUtils.createIntent(this, T::class.java, params)

inline fun <reified T : Any> Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
        ContextUtils.createIntent(activity, T::class.java, params)

inline fun <reified T : Activity> android.support.v4.app.Fragment.startActivity(vararg params: Pair<String, Any?>) =
        ContextUtils.startActivity(context!!, T::class.java, params)

inline fun <reified T : Activity> android.support.v4.app.Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        startActivityForResult(ContextUtils.createIntent(context!!, T::class.java, params), requestCode)

inline fun <reified T : Service> android.support.v4.app.Fragment.startService(vararg params: Pair<String, Any?>) =
        ContextUtils.startService(context!!, T::class.java, params)


inline fun <reified T : Service> android.support.v4.app.Fragment.stopService(vararg params: Pair<String, Any?>) =
        ContextUtils.stopService(context!!, T::class.java, params)

inline fun <reified T : Any> android.support.v4.app.Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
        ContextUtils.createIntent(context!!, T::class.java, params)

//endregion

//region 其他
/**
 * 检测是否处于debug模式。
 */
fun Context.isDebug(): Boolean {
    return try {
        this.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (e: Exception) {
        false
    }
}

/**
 * 获取 Application MetaData
 *
 * @param metaName
 * @param <T>
 * @return
</T> */
fun <T> Context.getApplicationMetaData(metaName: String): T? {
    try {
        return this.packageManager.getApplicationInfo(this.packageName, PackageManager.GET_META_DATA).metaData[metaName] as T
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}


/**
 * get App versionCode
 * @param context
 * @return
 */
fun Context.getVersionCode(): Int {
    try {
        return this.packageManager.getPackageInfo(this.packageName, 0).versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return 0
}

/**
 * get App versionName
 * @param context
 * @return
 */
fun Context.getVersionName(): String {
    try {
        return this.packageManager.getPackageInfo(this.packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return ""
}


//endregion