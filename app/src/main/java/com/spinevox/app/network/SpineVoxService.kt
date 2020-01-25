package com.spinevox.app.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpineVoxService {

    private const val BASE_URL = "https://spinevox-dev.i-bionic.com/"

    //const val API_KEY = "XZg8Ra5ueD4TTibr4mF6L766jaIVRXnY"
    //const val API_KEY = "2UDAgUayuZdghHXoF4zfqqtd1z3V7Qla"

    fun getService(context: Context, isWithAuthHeader: Boolean = false): SpineVoxApi {
//        val cache = Cache(context.cacheDir, cacheSize)

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
//            .cache(cache)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request()

//                if (isWithAuthHeader) {
//                    request.newBuilder().header("Authorization","JWT $authtoken")
//                }

                //request.newBuilder().header("Cache-Control", "public, max-age=$onlineCacheAge").build()
                //request.newBuilder().header("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJoYXNoIjoiYTk3YjlmYjU3NGU5NDEwMjk2YzFlYWYzNDg4NDgzYjkiLCJleHAiOjE1MzQ3NzI4ODMsIm9yaWdfaWF0IjoxNTM0NzcyNTgzfQ.KLmWRfp1g5BVzR79d-ZK9Xzf-DmOKhRtr8cYO-Sx4UNyyKIU2pgpdjudf5tUef1pEZcleETKOS_dziPE7pDRVQ")
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpineVoxApi::class.java)
    }

    const val authtoken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJoYXNoIjoiYTk3YjlmYjU3NGU5NDEwMjk2YzFlYWYzNDg4NDgzYjkiLCJleHAiOjE1MzQ3NzI4ODMsIm9yaWdfaWF0IjoxNTM0NzcyNTgzfQ.KLmWRfp1g5BVzR79d-ZK9Xzf-DmOKhRtr8cYO-Sx4UNyyKIU2pgpdjudf5tUef1pEZcleETKOS_dziPE7pDRVQ"

}