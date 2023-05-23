package br.com.stockmarcketapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitConfig {

    private const val BASE_URL = "https://alphavantage.co"

    fun create() : StockApi {
        val loggerInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(AuthenticateInterceptor())
            .addInterceptor(loggerInterceptor)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(StockApi::class.java)
    }
}