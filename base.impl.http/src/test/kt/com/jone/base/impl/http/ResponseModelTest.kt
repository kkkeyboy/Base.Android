package com.jone.base.impl.http

import com.jone.base.http.RequestParams
import com.jone.base.http.ResponseModel
import org.junit.Before
import org.junit.Test
import java.lang.reflect.ParameterizedType

class ResponseModelTest {
    private lateinit var response: ResponseModel

    @Before
    fun initData() {
        response = ResponseModel("""{
        "hash160":"637cbd9a43050c3c2066f41af2a4444e32229e15",
        "address":"1A53L872wUQMWi1aLzFWWGYgKoLaYadyAb",
        "n_tx":0,
        "total_received":0,
        "total_sent":0,
        "final_balance":0,
        "txs":[]
    }""")
    }

    @Test
    fun getPropertyInJson()
    {
//        response.getPropertyInJson<Long>("final_balance")


        val dd = object : Hehe<ABA>() {

        }.typeD
    }


    internal abstract class Hehe<T> {
        val typeD: Class<*>
            get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }

    internal class ABA
}