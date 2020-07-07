package com.wiz.alfacart.util

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.wiz.alfacart.base.CONST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiUtil {
    val retrofit: Retrofit
    val LOG_LEVEL = HttpLoggingInterceptor.Level.BODY

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(CONST.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private val okHttpClient: OkHttpClient
        get() {
            val logging = HttpLoggingInterceptor()
            val clientBuilder = OkHttpClient.Builder()
            logging.level = LOG_LEVEL
            clientBuilder.addInterceptor(logging)
            return clientBuilder.build()
        }

    private fun clientAllowAll(): OkHttpClient {
        return try {
            val trustAllCerts: Array<TrustManager> = arrayOf(MyManager())
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val logging = HttpLoggingInterceptor()
            logging.level = LOG_LEVEL
            OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())
                .addInterceptor(logging)
                .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    class MyManager : X509TrustManager {

        override fun checkServerTrusted(
            p0: Array<out java.security.cert.X509Certificate>?,
            p1: String?
        ) {
            //allow all
        }

        override fun checkClientTrusted(
            p0: Array<out java.security.cert.X509Certificate>?,
            p1: String?
        ) {
            //allow all
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
        }
    }
}
