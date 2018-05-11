package cf.jone.test

import android.support.annotation.CallSuper
import com.jone.base.http.BaseHttpResponseCallback
import com.jone.base.http.RequestParams
import com.jone.base.http.ResponseModel

/**
 * Created by Jone on 2018/4/20.
 */
abstract class CommonResponseCallback<T : BaseResponseBean> : BaseHttpResponseCallback<T>()
{
    companion object
    {
        fun <T : BaseResponseBean> responseCallback(successBlock: CommonResponseCallback<T>.() -> Boolean = {!getResponseBean<T>()?.success.isNullOrEmpty() },
                                                    preExecuteBlock: CommonResponseCallback<T>.() -> Unit = {},
                                                    callbackBlock: CommonResponseCallback<T>.(response: CommonResponseCallback<T>) -> Unit): CommonResponseCallback<T>
        {
            return object : CommonResponseCallback<T>()
            {
                override val isSuccess: Boolean
                    get() = successBlock.invoke(this)


                override fun onPreExecute(params: RequestParams)
                {
                    super.onPreExecute(params)
                    preExecuteBlock.invoke(this)
                }

                override fun onSuccess()
                {
                    callbackBlock.invoke(this, this)
                }
            }
        }

    }

    override val isSuccess: Boolean
        get() = !getResponseBean<T>()?.success.isNullOrEmpty()

    @CallSuper
    override fun onPreExecute(params: RequestParams)
    {
    }

    override fun onProgress(progress: Float)
    {
    }

    @CallSuper
    override fun onError(e: Exception)
    {
    }

    @CallSuper
    override fun onFinish()
    {
    }

}