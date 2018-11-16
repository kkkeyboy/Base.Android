package cf.jone.test

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cf.jone.test.CommonResponseCallback.Companion.responseCallback
import com.jone.base.binding.command.BaseCommand.Companion.command
import com.jone.base.binding.command.ICommand
import com.jone.base.http.HttpHelper
import com.jone.base.utils.LogUtils
import com.jone.base.utils.extend.logI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ViewDataBinding>(MainActivity@ this, R.layout.activity_main)
        binding.setVariable(BR.viewModel, this@MainActivity)

        LogUtils.init(this)

//        LiveDataEventBus.registerWithKeyPair("ddd", Test::class.java).observe(this@MainActivity, Observer<Test> {
//            it?.msg?.logI()
//        })

        LiveDataEventBus.register<Test>(this){
            it.msg.logI()
        }

//        LiveDataEventBus.register<Test>(this){
//            ("23333:"+it.msg).logI()
//        }

        LiveDataEventBus.register<BaseResponseBean>(this,true){
            "response: ${it.success}".logI()
        }
    }

    val testText = ObservableField<String>("Click")

    val TestCommand: ICommand by lazy {
        command<Any?> { _ ->

            HttpHelper.post("http://api.k780.com/").params("haha", "test")
                    .execute(responseCallback<BaseResponseBean> { response ->

                        LogUtils.i(response)

                        Handler().postDelayed({
                            LiveDataEventBus.post(Test("ddddddddwwwwwwww"),true)
                            LiveDataEventBus.post(response.getResponseBean<BaseResponseBean>()?:BaseResponseBean(response.toString()))

                            LiveDataEventBus.unregister<BaseResponseBean>(this@MainActivity)
                        },2000)
                    })

//            HttpHelper.get("https://blockchain.info/rawaddr/1A53L872wUQMWi1aLzFWWGYgKoLaYadyAb").execute(CommonResponseCallback.responseCallback<BaseResponseBean> { response ->
//               val final_balance = response.getResponseJsonObject()?.getLong("final_balance")
//                response.getPropertyInJson<Long>("final_balance")
//                LogUtils.i(response)
//            })
        }
    }


    class Test(val msg: String)

}
