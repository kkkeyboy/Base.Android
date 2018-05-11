package com.jone.base.impl.http

import com.jone.base.http.IHttpResponseCallback
import com.jone.base.http.RequestParams
import com.jone.base.utils.FileUtils
import com.jone.base.utils.extend.log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by Jone on 2018/4/16.
 */
class OkHttpDownloadCallbackHandler(val requestParams: RequestParams, val responseCallback: IHttpResponseCallback?) : Callback
{
    override fun onFailure(call: Call, e: IOException)
    {
        e.log()
        responseCallback?.onError(e)
    }

    @Throws(IOException::class) override fun onResponse(call: Call, response: Response)
    {
        //downloadfile
        var fileOutputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        try
        {
            fileOutputStream = FileOutputStream(File(requestParams.downLoadFilePath))
            val totalSize = response.body()!!.contentLength()
            var temp: Long = 0
            inputStream = response.body()!!.byteStream()
            val bys = ByteArray(1024)
            var len = inputStream!!.read(bys)
            while (len != -1)
            {
                temp += len.toLong()
                val progress = temp * 1.0f / totalSize * 100
                fileOutputStream.write(bys, 0, len)
                fileOutputStream.flush()
                len = inputStream.read(bys)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        finally
        {
            FileUtils.closeIO(fileOutputStream, inputStream)
        }
    }

}