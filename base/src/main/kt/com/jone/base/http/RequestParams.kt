package com.jone.base.http

import com.jone.base.http.executor.IExecutor
import java.io.File
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Jone.L on 2017/12/4.
 */
class RequestParams {
   var url: String = ""
        private set(value) {
            field = value
        }
    var header: HashMap<String, String> = HashMap()
        private set(value) {
            field = value
        }
    var requestParams: HashMap<String, String> = HashMap()
        private set(value) {
            field = value
        }
    var files: HashMap<String, File> = HashMap()
        private set(value) {
            field = value
        }
    var charset = Charset.forName("UTF-8")
        private set(value) {
            field = value
        }
    var body: String? = null
        private set(value) {
            field = value
        }
    var downLoadFilePath: String? = null
        private set(value) {
            field = value
        }
    var callback: IHttpResponseCallback? = null
        private set(value) {
            field = value
        }
    var tag: Any? = null
        private set(value) {
            field = value
        }

    class Builder(private var method: Int = Method.GET, private var url: String, private val executor: IExecutor) {
        private val header = HashMap<String, String>()
        private val requestParams = HashMap<String, String>()
        private val files = HashMap<String, File>()
        private var charset = Charset.forName("UTF-8")
        var downLoadFilePath: String? = null
        private var body: String? = null
        var tag: Any? = null

        fun header(key: String, value: String): Builder {
            this.header.put(key, value)
            return this
        }

        fun params(key: String, value: String): Builder {
            this.requestParams.put(key, value)
            return this
        }

        fun files(key: String, file: File): Builder {
            this.files.put(key, file)
            return this
        }

        fun defaultCharset(charset: Charset): Builder {
            this.charset = charset
            return this
        }

        fun body(body: String): Builder {
            this.body = body
            return this
        }

        fun downLoadFilePath(downLoadFilePath: String): Builder {
            this.downLoadFilePath = downLoadFilePath
            return this
        }

        fun tag(tag: Any?): Builder {
            this.tag = tag
            return this
        }


        private fun build(): RequestParams {
            val obj = RequestParams()
            obj.url = this.url
            obj.header = this.header
            obj.requestParams = this.requestParams
            obj.files = this.files
            obj.charset = this.charset
            obj.body = this.body
            obj.downLoadFilePath = this.downLoadFilePath
            obj.tag = tag
            return obj
        }

        fun execute(responseCallback: IHttpResponseCallback? = null) {
            when (method) {
                Method.GET -> executor.doGet(build(), responseCallback)
                Method.POST -> executor.doPost(build(), responseCallback)
                Method.POST_JSON -> executor.doPostJson(build(), responseCallback)
                Method.UPLOAD -> executor.doUploadFile(build(), responseCallback)
                Method.DOWNLOAD -> executor.doDownLoad(build(), responseCallback)
            }

        }
    }

    /* 请求方法*/
    object Method {
        const val GET = 0x0001
        const val POST = 0x0002
        const val POST_JSON = 0x0003
        const val UPLOAD = 0x0004
        const val DOWNLOAD = 0x0005
    }
}