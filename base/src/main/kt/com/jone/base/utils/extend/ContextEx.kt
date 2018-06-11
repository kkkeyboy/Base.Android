package com.jone.base.utils.extend

import android.app.Activity
import android.app.Fragment
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import com.jone.base.utils.ContextUtils


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

inline fun <reified T: Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
        ContextUtils.startActivity(this, T::class.java, params)

inline fun <reified T: Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) =
        ContextUtils.startActivity(activity, T::class.java, params)

inline fun <reified T: Activity> Activity.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        ContextUtils.startActivityForResult(this, T::class.java, requestCode, params)

inline fun <reified T: Activity> Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        startActivityForResult(ContextUtils.createIntent(activity, T::class.java, params), requestCode)

inline fun <reified T: Service> Context.startService(vararg params: Pair<String, Any?>) =
        ContextUtils.startService(this, T::class.java, params)

inline fun <reified T: Service> Fragment.startService(vararg params: Pair<String, Any?>) =
        ContextUtils.startService(activity, T::class.java, params)

inline fun <reified T : Service> Context.stopService(vararg params: Pair<String, Any?>) =
        ContextUtils.stopService(this, T::class.java, params)

inline fun <reified T : Service> Fragment.stopService(vararg params: Pair<String, Any?>) =
        ContextUtils.stopService(activity, T::class.java, params)

inline fun <reified T: Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
        ContextUtils.createIntent(this, T::class.java, params)

inline fun <reified T: Any> Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
        ContextUtils.createIntent(activity, T::class.java, params)

inline fun <reified T: Activity> android.support.v4.app.Fragment.startActivity(vararg params: Pair<String, Any?>) =
        ContextUtils.startActivity(activity, T::class.java, params)

inline fun <reified T: Activity> android.support.v4.app.Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        startActivityForResult(ContextUtils.createIntent(activity, T::class.java, params), requestCode)

inline fun <reified T: Service> android.support.v4.app.Fragment.startService(vararg params: Pair<String, Any?>) =
        ContextUtils.startService(activity, T::class.java, params)


inline fun <reified T : Service> android.support.v4.app.Fragment.stopService(vararg params: Pair<String, Any?>) =
        ContextUtils.stopService(activity, T::class.java, params)

inline fun <reified T: Any> android.support.v4.app.Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
        ContextUtils.createIntent(activity, T::class.java, params)

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearWhenTaskReset(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) }

/**
 * Add the [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.excludeFromRecents(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

/**
 * Add the [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_ANIMATION] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_HISTORY] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

/**
 * Add the [Intent.FLAG_ACTIVITY_SINGLE_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }