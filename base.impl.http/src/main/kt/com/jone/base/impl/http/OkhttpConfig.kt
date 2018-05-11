package com.jone.base.impl.http

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;

/**
 * Created by Jone on 2018/4/16.
 */
class OkhttpConfig {
    var retryOnConnectionFailure: Boolean = false
        private set(value) {
            field = value
        }
    var connectTimeout: Long = 0
        private set(value) {
            field = value
        }
    var cache: Cache? = null
        private set(value) {
            field = value
        }
    var readTimeout: Long = 0
        private set(value) {
            field = value
        }
    var writeTimeout: Long = 0
        private set(value) {
            field = value
        }
    var interceptors = ArrayList<Interceptor>()
        private set(value) {
            field = value
        }
    var networkInterceptors = ArrayList<Interceptor>()
        private set(value) {
            field = value
        }
    var sslSocketFactory: SSLSocketFactory? = null
        private set(value) {
            field = value
        }
    var x509TrustManager: X509TrustManager? = null
        private set(value) {
            field = value
        }


    class Builder {

        private var retryOnConnectionFailure: Boolean = false
        private var connectTimeout: Long = 0
        private var cache: Cache? = null
        private var readTimeout: Long = 0
        private var writeTimeout: Long = 0
        private val interceptors = ArrayList<Interceptor>()
        private val networkInterceptors = ArrayList<Interceptor>()
        private var sslSocketFactory: SSLSocketFactory? = null
        private var x509TrustManager: X509TrustManager? = null

        fun retryOnConnectionFailure(retryOnConnectionFailure: Boolean): Builder {
            this.retryOnConnectionFailure = retryOnConnectionFailure
            return this
        }

        fun connectTimeout(connectTimeout: Long): Builder {
            this.connectTimeout = connectTimeout
            return this
        }

        fun cache(cache: Cache): Builder {
            this.cache = cache
            return this
        }

        fun readTimeout(readTimeout: Long): Builder {
            this.readTimeout = readTimeout
            return this
        }

        fun writeTimeout(writeTimeout: Long): Builder {
            this.writeTimeout = writeTimeout
            return this
        }

        fun interceptors(interceptor: Interceptor): Builder {
            this.interceptors.add(interceptor)
            return this
        }

        fun networkInterceptors(interceptor: Interceptor): Builder {
            this.networkInterceptors.add(interceptor)
            return this
        }

        fun sslSocketFactory(sslSocketFactory: SSLSocketFactory): Builder {
            this.sslSocketFactory = sslSocketFactory
            return this
        }

        fun x509TrustManager(x509TrustManager: X509TrustManager): Builder {
            this.x509TrustManager = x509TrustManager
            return this
        }

        fun build(): OkhttpConfig {
            val okhttpConfig = OkhttpConfig()
            okhttpConfig.cache = this.cache
            okhttpConfig.connectTimeout = this.connectTimeout
            okhttpConfig.interceptors = this.interceptors
            okhttpConfig.networkInterceptors = this.networkInterceptors
            okhttpConfig.readTimeout = this.readTimeout
            okhttpConfig.writeTimeout = this.writeTimeout
            okhttpConfig.retryOnConnectionFailure = this.retryOnConnectionFailure
            okhttpConfig.sslSocketFactory = this.sslSocketFactory
            okhttpConfig.x509TrustManager = this.x509TrustManager
            return okhttpConfig
        }

    }
}