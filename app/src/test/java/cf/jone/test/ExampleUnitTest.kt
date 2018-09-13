package cf.jone.test

import com.jone.base.http.RequestParams
import org.junit.Test

import org.junit.Assert.*
import java.lang.reflect.ParameterizedType

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testType() {
      val dd=  CommonResponseCallback.responseCallback<BaseResponseBean> { response -> }.getResponseModelType()
        val aa = CommonResponseCallback.responseCallback<AAA> { response -> }.getResponseModelType()

         object : CommonResponseCallback<BaseResponseBean>() {

            override fun onPreExecute(params: RequestParams) {
                super.onPreExecute(params)
            }

            override fun onSuccess() {
            }
        }.typeD
    }

    class AAA : BaseResponseBean("22")
}
