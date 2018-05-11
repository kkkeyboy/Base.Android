package com.jone.base.impl.http

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri
import com.jone.base.http.HttpRequestStrategy
import com.jone.base.utils.extend.logI

/**
 * Created by Jone on 2018/4/16.
 */
internal class InitializationHelper : ContentProvider() {
    override fun onCreate(): Boolean {

        HttpRequestStrategy.seHttpExecutorImpl(OkHttpExecutorFactory().create())
        "com.jone.base.impl.http inited".logI()
        return false
    }


    //region 空实现
    override fun insert(p0: Uri?, p1: ContentValues?) = null

    override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?) = null

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?) = 0

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?) = 0

    override fun getType(p0: Uri?) = null
    //endregion
}