package com.jone.base.utils

import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import com.jone.base.protocol.AsyncCancelToken
import com.jone.base.protocol.IProgress
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.text.DecimalFormat


/**
 * Created by Jone on 2017/12/4.
 */
object FileUtils
{
    /**
     * 计算SD卡的剩余空间
     *
     * @return 返回-1，说明没有安装sd卡
     */
    fun getExternalStorageFreeDiskSpace(): Long
    {
        var freeSpace: Long = 0
        if (isExternalStorageExists())
        {
            try
            {
                val path = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.getPath())
                val blockSize = stat.blockSize.toLong()
                val availableBlocks = stat.availableBlocks.toLong()
                freeSpace = availableBlocks * blockSize / 1024
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

        }
        else
        {
            return -1
        }
        return freeSpace
    }

    /**
     * 新建目录
     *
     * @param forderName
     * @return
     */
    fun createFolder(forderName: String): Boolean
    {
        val status: Boolean
        if (forderName != "")
        {
            val path = Environment.getExternalStorageDirectory()
            val newPath = File(path.toString() + forderName)
            status = newPath.mkdir()
        }
        else status = false
        return status
    }

    /**
     * 检查是否安装SD卡
     *
     * @return
     */
    fun isExternalStorageExists(): Boolean
    {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    fun getFileSize(filePath: String): Long
    {
        var size: Long = 0

        val file = File(filePath)
        if (file.exists())
        {
            size = file.length()
        }
        return size
    }

    /**
     * 获取目录文件大小，包含内部文件夹
     *
     * @param dir
     * @return
     */
    fun getFolderSize(dir: File?): Long
    {
        if (dir == null)
        {
            return 0
        }
        if (!dir.isDirectory())
        {
            return if (dir.exists())
            {
                dir.length()
            }
            else 0
        }
        var dirSize: Long = 0
        val files = dir.listFiles()
        for (file in files)
        {
            if (file.isFile())
            {
                dirSize += file.length()
            }
            else if (file.isDirectory())
            {
                dirSize += file.length()
                dirSize += getFolderSize(file) // 递归调用继续统计

            }
        }
        return dirSize
    }

    /**
     * 获取目录文件大小，不包含内部文件夹
     *
     * @param dir
     * @return
     */
    fun getFolderSizeWithoutInsideFolder(dir: File?): Long
    {
        if (dir == null)
        {
            return 0
        }
        if (!dir.isDirectory())
        {
            return if (dir.exists())
            {
                dir.length()
            }
            else 0
        }
        var dirSize: Long = 0
        val files = dir.listFiles()
        for (file in files)
        {
            if (file.isFile())
            {
                dirSize += file.length()
            }
            else if (file.isDirectory())
            {
                continue
            }
        }
        return dirSize
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param dir
     * @return
     */
    fun deleteFolder(dir: File?): Boolean
    {
        if (dir == null)
        {
            return false
        }

        val checker = SecurityManager()
        checker.checkDelete(dir.getAbsolutePath())

        if (!dir.isDirectory())
        {
            return if (dir.exists())
            {
                dir.delete()
            }
            else false
        }


        var status = true

        val files = dir.listFiles()
        for (file in files)
        {
            if (file.isFile())
            {
                status = file.delete()
            }
            else if (file.isDirectory())
            {
                status = deleteFolder(file)
            }
        }
        return status
    }

    fun deleteFolderAsync(dir: File)
    {
        Thread(Runnable { deleteFolder(dir) }).start()
    }


    /**
     * 异步获取目录文件大小，包含内部文件夹
     *
     * @param dir
     * @return
     */
    fun getFolderSizeAsync(dir: File?, progress: IProgress<Long>)
    {

        if (dir == null)
        {
            progress.onComplete()
            return
        }
        if (!dir.isDirectory)
        {
            if (dir.exists())
            {
                progress.onProgressChanged(dir.length())
                progress.onComplete()
                return
            }
            progress.onComplete()
            return
        }

        Thread(Runnable {
            val mainHandler = Handler(Looper.getMainLooper())
            val files = dir.listFiles()
            for (file in files)
            {
                mainHandler.post(Runnable { progress.onProgressChanged(file.length()) })
                if (file.isDirectory)
                {
                    getFolderSizeAsync(file, progress) // 递归调用继续统计

                }
            }
            mainHandler.post(Runnable { progress.onComplete() })
        }).start()
    }

    /**
     * 异步获取目录文件大小，包含内部文件夹(加间隔时间
     *
     * @param dir
     * @return
     */
    fun getFolderSizeAsync(dir: File?, progress: IProgress<Long>, interval: Long, cancelToken: AsyncCancelToken? = null)
    {

        if (dir == null)
        {
            progress.onComplete()
            return
        }
        if (!dir.isDirectory)
        {
            if (dir.exists())
            {
                progress.onProgressChanged(dir.length())
                progress.onComplete()
                return
            }
            progress.onComplete()
            return
        }

        Thread(Runnable {
            val mainHandler = Handler(Looper.getMainLooper())
            try
            {
                val files = dir.listFiles()
                for (file in files)
                {
                    if (cancelToken?.isCanceled ?: false)
                    {
                        return@Runnable
                    }
                    mainHandler.post(Runnable { progress.onProgressChanged(file.length()) })
                    if (file.isDirectory)
                    {
                        getFolderSizeAsync(file, progress, interval, cancelToken) // 递归调用继续统计

                    }
                }
                Thread.sleep(interval)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

            mainHandler.post(Runnable { progress.onComplete() })
        }).start()
    }

    /**
     * 异步删除目录(包括：目录里的所有文件)
     *
     * @param dir
     * @return
     */
    fun deleteFolderAsync(dir: File?, progress: IProgress<Long>)
    {
        if (dir == null)
        {
            progress.onComplete()
            return
        }

        val checker = SecurityManager()
        checker.checkDelete(dir.getAbsolutePath())

        if (!dir.isDirectory)
        {
            if (dir.exists())
            {
                progress.onProgressChanged(dir.length())
                dir.delete()
                progress.onComplete()
                return
            }
            progress.onComplete()
            return
        }
        Thread(Runnable {
            val mainHandler = Handler(Looper.getMainLooper())
            val files = dir.listFiles()
            for (file in files)
            {
                if (file.isFile)
                {
                    val deletedFileSize = file.length()
                    if (file.delete())
                    {
                        mainHandler.post(Runnable { progress.onProgressChanged(deletedFileSize) })
                    }
                }
                else if (file.isDirectory)
                {
                    deleteFolderAsync(file, progress)
                }
            }
            mainHandler.post(Runnable { progress.onComplete() })
        }).start()
    }

    /**
     * 异步删除目录(包括：目录里的所有文件) interval:间隔时间
     *
     * @param dir
     * @return
     */
    fun deleteFolderAsync(dir: File?, progress: IProgress<Long>, interval: Long, cancelToken: AsyncCancelToken? = null)
    {
        if (dir == null)
        {
            progress.onComplete()
            return
        }

        val checker = SecurityManager()
        checker.checkDelete(dir.absolutePath)

        if (!dir.isDirectory)
        {
            if (dir.exists())
            {
                progress.onProgressChanged(dir.length())
                dir.delete()
                progress.onComplete()
                return
            }
            progress.onComplete()
            return
        }
        Thread(Runnable {
            val mainHandler = Handler(Looper.getMainLooper())
            val files = dir.listFiles()
            for (file in files)
            {
                if (cancelToken?.isCanceled ?: false)
                {
                    return@Runnable
                }
                if (file.isFile())
                {
                    val deletedFileSize = file.length()
                    if (file.delete())
                    {
                        mainHandler.post(Runnable { progress.onProgressChanged(deletedFileSize) })
                        try
                        {
                            Thread.sleep(interval)
                        }
                        catch (e: InterruptedException)
                        {
                            e.printStackTrace()
                        }

                    }
                }
                else if (file.isDirectory)
                {
                    deleteFolderAsync(file, progress, interval, cancelToken)
                }
            }
            mainHandler.post(Runnable { progress.onComplete() })
        }).start()
    }

    /* 转换文件大小
     *
             * @param fileS
     * @return B/KB/MB/GB
     */
    fun formatFileSize(fileS: Long): String
    {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        if (fileS <= 0)
        {
            fileSizeString = ""
        }
        else if (fileS < 1024)
        {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        }
        else if (fileS < 1048576)
        {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"
        }
        else if (fileS < 1073741824)
        {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"
        }
        else
        {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    fun closeIO(vararg closeables: Closeable?)
    {
        closeables.forEach { item ->
            try
            {
                item?.close()
            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }
        }
    }


}