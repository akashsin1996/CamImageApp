package com.skydroid.camimageapp

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Akash Singh on 02-June-2023.
 */
object ApiClientGenric {

    val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
            val okkHttpClient = OkHttpClient.Builder()
            okkHttpClient.readTimeout(1000, TimeUnit.SECONDS)
            okkHttpClient.connectTimeout(1000, TimeUnit.SECONDS)
            okkHttpClient.addInterceptor(logging)


            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()
    var gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: ApiInterface by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build()

        retrofit.create(ApiInterface::class.java)
    }
}