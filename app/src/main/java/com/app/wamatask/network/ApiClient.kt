package com.app.mediq.networks

import android.content.Context
import android.util.Log
import com.app.wamatask.App
import com.app.wamatask.networks.ConnectivityInterceptor
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.ArrayList


class ApiClient {

    private var SERVER_URL1: String? = null
    var client: Retrofit? = null
    private var SERVER_URL3: String? = null
    private val TAG = "ApiClient"

    private val NO_OF_LOG_CHAR = 1000


    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private fun init(ctx: Context?) {
        instance!!.SERVER_URL1 = "https://newsapi.org/v2/"
        instance!!.client = Retrofit.Builder()
            .baseUrl(instance!!.SERVER_URL1!!)
            .client(getOkHttpClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    companion object {

        private var instance: ApiClient? = null

        fun get(): ApiClient {
            if (instance == null) instance = getInstance()
            return instance as ApiClient
        }

        @Synchronized
        private fun getInstance(): ApiClient {
            if (instance == null) {
                instance = ApiClient()
                instance!!.init(App.appContext)
            }
            return instance as ApiClient
        }
    }

    private class SessionCookieJar : CookieJar {


        private var cookies: List<Cookie>? = null

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            this.cookies = ArrayList(cookies)
        }


        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            if (cookies != null) {
                return cookies as List<Cookie>
            }
            return Collections.emptyList()
        }
    }


    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        val trustAllCerts =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        var sslSocketFactory = sslContext.socketFactory

        val oktHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(ConnectivityInterceptor(App.appContext!!))
            .cookieJar(SessionCookieJar())

        oktHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor())
        oktHttpClientBuilder.addInterceptor { chain ->
            var request = chain.request()
            printPostmanFormattedLog(request)
            var response = chain.proceed(request)
            response
        }

        oktHttpClientBuilder.sslSocketFactory(
            sslSocketFactory,
            trustAllCerts[0] as X509TrustManager
        )
        oktHttpClientBuilder.hostnameVerifier(HostnameVerifier { hostname, session -> true })

        return oktHttpClientBuilder
    }

    private fun printPostmanFormattedLog(request: Request) {
        try {
            val allParams: String
            allParams = if (request.method == "GET" || request.method == "DELETE") {
                request.url.toString().substring(
                    request.url.toString().indexOf("?") + 1,
                    request.url.toString().length
                )
            } else {
                val buffer = Buffer()
                request.body!!.writeTo(buffer)
                buffer.readString(Charset.forName("UTF-8"))
            }
            val params =
                allParams.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val paramsString = StringBuilder("\n")
            for (param in params) {
                paramsString.append(decode(param.replace("=", ":")))
                paramsString.append("\n")
            }
            Log.e(TAG, paramsString.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun decode(url: String): String {
        return try {
            var prevURL = ""
            var decodeURL = url
            while (prevURL != decodeURL) {
                prevURL = decodeURL
                decodeURL = URLDecoder.decode(decodeURL, "UTF-8")
            }
            decodeURL
        } catch (e: UnsupportedEncodingException) {
            "Issue while decoding" + e.message
        }
    }

    private fun getHttpLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (message.length > NO_OF_LOG_CHAR) {
                    for (noOfLogs in 0..message.length / NO_OF_LOG_CHAR) {
                        if (noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR < message.length) {
                            Log.e(
                                TAG,
                                message.substring(
                                    noOfLogs * NO_OF_LOG_CHAR,
                                    noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR
                                )
                            )
                        } else {
                            Log.e(TAG, message.substring(noOfLogs * NO_OF_LOG_CHAR, message.length))
                        }
                    }
                } else {
                    Log.e(TAG, message)
                }
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

}