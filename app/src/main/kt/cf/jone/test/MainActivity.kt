package cf.jone.test

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cf.jone.test.CommonResponseCallback.Companion.responseCallback
import com.jone.base.binding.command.BaseCommand.Companion.command
import com.jone.base.binding.command.ICommand
import com.jone.base.http.HttpHelper
import com.jone.base.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ViewDataBinding>(MainActivity@ this, R.layout.activity_main)
        binding.setVariable(BR.viewModel, this@MainActivity)

        LogUtils.init(this)

    }

    val testText = ObservableField<String>("Click")

    val TestCommand: ICommand by lazy {
        command<Any?> { _ ->
            HttpHelper.post("http://api.k780.com/").params("haha", "test")
                    .execute(responseCallback<BaseResponseBean> { response ->

                    LogUtils.i(response)
            })
        }
    }

}
