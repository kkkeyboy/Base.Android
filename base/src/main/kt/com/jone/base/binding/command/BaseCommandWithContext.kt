package com.jone.base.binding.command

import android.app.Activity
import android.content.Context
import com.jone.base.utils.ContextUtils

abstract class BaseCommandWithContext<T : Any?> : BaseCommand<T>() {
    var ctx: Context? = null
        internal set(value) {
            field = value
        }

    inline fun <reified T : Activity> startActivity(vararg params: Pair<String, Any?>) {
        if (ctx != null) {
            ContextUtils.startActivity(ctx!!, T::class.java, params)
        }
    }
}