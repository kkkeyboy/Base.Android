package com.jone.base.protocol


/**
 * Created by Jone.L on 2017/12/4.
 */
interface IProgress<in T>
{
    fun onProgressChanged(progress: T)

    fun onComplete()
}