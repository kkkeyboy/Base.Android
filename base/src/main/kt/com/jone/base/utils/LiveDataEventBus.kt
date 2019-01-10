package com.jone.base.utils

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Looper
import android.support.v4.util.ArrayMap
import android.support.v4.util.ArraySet


object LiveDataEventBus {
    //存储订阅者类名和消息类名
    private var mCacheKeys = ArrayMap<String, ArraySet<String>>()
    //存储消息类名做key
    private var mCacheBus = ArrayMap<String, BusLiveData<*>>()

    fun clearAll() {
        this.mCacheKeys.clear()
        this.mCacheBus.clear()
    }

    fun unregister(owner: LifecycleOwner, messageEventName: String = "") {
        val keysKey = owner.javaClass.name
        if (mCacheKeys.contains(keysKey)) {
            val removedMessageEvent = if (messageEventName.isEmpty()) mCacheKeys.remove(keysKey) else {
                mCacheKeys[keysKey]!!.clear()
                ArraySet<String>().apply { add(messageEventName) }
            }

            //缓存key里面没有对应的event 则从bus里面移除
            removedMessageEvent?.forEach { removed ->
                if (!mCacheKeys.values.any { it.contains(removed) }) {
                    mCacheBus.remove(removed)
                }
            }
        }
    }

    fun isRegistered(owner: LifecycleOwner, messageEventName: String = ""): Boolean {
        val keysKey = owner.javaClass.name
        return mCacheKeys.containsKey(keysKey) && mCacheKeys[keysKey]!!.contains(messageEventName) && mCacheBus.containsKey(messageEventName)
    }


    fun <T> registerWithKeyPair(owner: LifecycleOwner, messageEventName: String,messageCacheName:String, needCurrentDataWhenNewObserve: Boolean = false): MutableLiveData<T> {
        return this.registerInfo(owner, messageEventName,messageCacheName, needCurrentDataWhenNewObserve)
    }

    private fun <T> registerInfo(owner: LifecycleOwner, messageEventName: String,messageCacheName:String, needCurrentDataWhenNewObserve: Boolean): MutableLiveData<T> {
        //缓存key不包含订阅者
        val keysKey = owner.javaClass.name
        if (!mCacheKeys.containsKey(keysKey)) {
            val setTmp = ArraySet<String>()
            mCacheKeys[keysKey] = setTmp.apply { add(messageCacheName) }
            owner.lifecycle.addObserver(LifecycleWatcher())
        } else if (!mCacheKeys[keysKey]!!.contains(messageCacheName)) {
            mCacheKeys[keysKey]!!.apply { add(messageCacheName) }
        }

        //存储订阅消息
        if (!mCacheBus.containsKey(messageEventName)) {
            mCacheBus[messageEventName] = BusLiveData<T>()
        }
        val data = mCacheBus[messageEventName]
        data!!.mNeedCurrentDataWhenFirstObserve = needCurrentDataWhenNewObserve
        return mCacheBus[messageEventName] as MutableLiveData<T>
    }

    fun getLiveData(messageEventName: String): MutableLiveData<*>? {
        return if (mCacheBus.contains(messageEventName)) {
            mCacheBus[messageEventName]
        } else null
    }


    // 比如BusLiveData 在添加 observe的时候，同一个界面对应的一个事件只能注册一次
    internal class BusLiveData<T>() : MutableLiveData<T>() {
        //首次注册的时候，是否需要当前LiveData 最新数据
        internal var mNeedCurrentDataWhenFirstObserve: Boolean = false

        //主动触发数据更新事件才通知所有Observer
        internal var mIsStartChangeData = false

        override fun setValue(value: T) {
            mIsStartChangeData = true
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mIsStartChangeData = true
            super.postValue(value)
        }

        //添加注册对应事件type的监听
        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            super.observe(owner, ObserverWrapper(observer, this))
        }

        //数据更新一直通知刷新
        override fun observeForever(observer: Observer<T>) {
            super.observeForever(observer)
        }

    }

    internal class ObserverWrapper<T>(private val observer: Observer<T>?, private val liveData: BusLiveData<T>) : Observer<T> {
        init {
            //mIsStartChangeData 可过滤掉liveData首次创建监听，之前的遗留的值
            liveData.mIsStartChangeData = liveData.mNeedCurrentDataWhenFirstObserve
        }

        override fun onChanged(t: T?) {
            if (liveData.mIsStartChangeData) {
                observer?.onChanged(t)

            }
        }
    }

    class LifecycleWatcher : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            LiveDataEventBus.unregister(owner)
            owner.lifecycle.removeObserver(this)
        }
    }
}

//注册事件
inline fun <reified T : Any> LiveDataEventBus.isRegistered(owner: LifecycleOwner): Boolean {
    return isRegistered(owner, T::class.java.name)
}

//注册事件
inline fun <reified T : Any> LiveDataEventBus.register(owner: LifecycleOwner,messageEventName:String = T::class.java.name, messageCacheName:String = messageEventName,isObserverForever: Boolean = false, needCurrentDataWhenNewObserve: Boolean = false, crossinline observerBlock: (changedData: T) -> Unit): MutableLiveData<T> {
    return registerWithKeyPair<T>(owner,messageEventName ,messageCacheName, needCurrentDataWhenNewObserve).let { liveData ->
        if (isObserverForever) {
            liveData.observeForever {
                it?.let(observerBlock)
            }
        } else {
            liveData.observe(owner, Observer<T> {
                it?.apply {
                    observerBlock.invoke(this)
                }
            })
        }

        liveData
    }
}

//取消注册
inline fun <reified T : Any> LiveDataEventBus.unregister(owner: LifecycleOwner) {
    unregister(owner, T::class.java.name)
}


//发送消息
fun LiveDataEventBus.post(event: Any,messageEventName:String = event.javaClass.name) {
    val isRunOnUI = Looper.getMainLooper().getThread() == Thread.currentThread()
    (getLiveData(messageEventName) as? MutableLiveData<Any>)?.let { if (isRunOnUI) it.postValue(event) else it.value = event }
}

