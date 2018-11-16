package cf.jone.test

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.v4.util.ArrayMap
import android.support.v4.util.ArraySet
import android.util.Log
import cf.jone.test.LiveDataEventBus.ObserverWrapper


object LiveDataEventBus {
    //存储订阅者类名和消息类名
    private var mCacheKeys = ArrayMap<String, ArraySet<String>>()
    //存储消息类名做key
    private var mCacheBus = ArrayMap<String, BusLiveData<*>>()

    fun clearAll() {
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


    fun <T> registerWithKeyPair(owner: LifecycleOwner, messageEventName: String, needCurrentDataWhenNewObserve: Boolean = false): MutableLiveData<T> {
        return this.registerInfo(owner, messageEventName, needCurrentDataWhenNewObserve)
    }

    private fun <T> registerInfo(owner: LifecycleOwner, messageEventName: String, needCurrentDataWhenNewObserve: Boolean): MutableLiveData<T> {
        //缓存key不包含订阅者
        val keysKey = owner.javaClass.name
        if (!mCacheKeys.containsKey(keysKey)) {
            val setTmp = ArraySet<String>()
            mCacheKeys[keysKey] = setTmp.apply { add(messageEventName) }
            owner.lifecycle.addObserver(LifecycleWatcher())
        } else if (!mCacheKeys[keysKey]!!.contains(messageEventName)) {
            mCacheKeys[keysKey]!!.apply { add(messageEventName) }
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
            Log.i("UuU", "LifecycleWatcher  onDestroy ${owner}")
            LiveDataEventBus.unregister(owner)
            owner.lifecycle.removeObserver(this)
        }
    }
}

//注册事件
inline fun <reified T : Any> LiveDataEventBus.register(owner: LifecycleOwner, isObserverForever: Boolean = false, needCurrentDataWhenNewObserve: Boolean = false, crossinline observerBlock: (changedData: T) -> Unit): MutableLiveData<T> {
    return registerWithKeyPair<T>(owner, T::class.java.name, needCurrentDataWhenNewObserve).let { liveData ->
        if (isObserverForever) {
            liveData.observeForever {
                it?.let(observerBlock)
            }
        } else {
            liveData.observe(owner, Observer<T> {
                it?.let(observerBlock)
            })
        }

        liveData
    }
}

//取消注册
inline fun <reified T : Any> LiveDataEventBus.unregister(owner: LifecycleOwner)
{
    unregister(owner,T::class.java.name)
}


//发送消息
fun LiveDataEventBus.post(event: Any, isRunOnUI: Boolean = false) {
    (getLiveData(event.javaClass.name) as? MutableLiveData<Any>)?.let { if (isRunOnUI) it.postValue(event) else it.value = event }
}

