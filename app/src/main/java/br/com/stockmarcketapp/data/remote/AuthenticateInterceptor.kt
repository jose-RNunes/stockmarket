package br.com.stockmarcketapp.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticateInterceptor : Interceptor {

    private companion object {
        const val API_KEY = "G1USXWKX272RK4BP"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url

        val newUrl = requestUrl.newBuilder()
            .addQueryParameter("apikey", API_KEY)
            .build()
        val requestBuilder = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(requestBuilder)
    }
}