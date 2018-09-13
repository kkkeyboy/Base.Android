package cf.jone.test

import android.support.annotation.CallSuper
import android.support.annotation.VisibleForTesting
import com.jone.base.http.BaseHttpResponseCallback
import com.jone.base.http.RequestParams
import com.jone.base.http.ResponseModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.WildcardType

/**
 * Created by Jone on 2018/4/20.
 */
@VisibleForTesting
abstract class CommonResponseCallback<T : BaseResponseBean> : BaseHttpResponseCallback<T>() {
    companion object {
        fun <T : BaseResponseBean> responseCallback(successBlock: CommonResponseCallback<in T>.() -> Boolean = { !getResponseBean<T>()?.success.isNullOrEmpty() },
                                                    preExecuteBlock: CommonResponseCallback<in T>.() -> Unit = {},
                                                    callbackBlock: CommonResponseCallback<in T>.(response: CommonResponseCallback<in T>) -> Unit): CommonResponseCallback<in T> {

            return object : CommonResponseCallback<T>() {
                override val isSuccess: Boolean
                    get() = successBlock.invoke(this)


                override fun onPreExecute(params: RequestParams) {
                    super.onPreExecute(params)
                    preExecuteBlock.invoke(this)
                }

                override fun onSuccess() {
                    callbackBlock.invoke(this, this)
                }

                protected override fun getOverrideResponseModelType(): Class<T> {
                    return (((callbackBlock.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as ParameterizedType).actualTypeArguments[0] as WildcardType).lowerBounds[0] as Class<T>
                }
            }
        }

    }

    override val isSuccess: Boolean
        get() = !getResponseBean<T>()?.success.isNullOrEmpty()

    @CallSuper
    override fun onPreExecute(params: RequestParams) {
    }

    override fun onProgress(progress: Float) {
    }

    @CallSuper
    override fun onError(e: Exception) {
    }

    @CallSuper
    override fun onFinish() {
    }


    open val typeD: Class<*>
        get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>

}