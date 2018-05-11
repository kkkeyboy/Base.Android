package com.jone.base.impl.http

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * Created by Jone on 2018/4/17.
 */
class ProgressRequestBody(val body: RequestBody):RequestBody() {
    private var bufferedSink: BufferedSink? = null

    override fun contentType(): MediaType?
    {
        return body.contentType()
    }

    @Throws(IOException::class) override fun contentLength(): Long
    {
        return body.contentLength()
    }

    @Throws(IOException::class) override fun writeTo(sink: BufferedSink)
    {
        if (bufferedSink == null)
        {
            bufferedSink = Okio.buffer(sink(sink))
        }
        body.writeTo(bufferedSink!!)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink!!.flush()

    }

    private fun sink(sink: Sink): Sink
    {
        return object : ForwardingSink(sink)
        {
            private var current: Long = 0
            private var total: Long = 0

            @Throws(IOException::class) override fun write(source: Buffer, byteCount: Long)
            {
                super.write(source, byteCount)
                if (total == 0L)
                {
                    total = contentLength()
                }
                current += byteCount
                val progress = current * 1.0f / total * 100

            }
        }
    }
}