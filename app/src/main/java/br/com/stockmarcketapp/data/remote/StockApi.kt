package br.com.stockmarcketapp.data.remote

import br.com.stockmarcketapp.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(@Query("symbol") symbol: String): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(@Query("symbol") symbol: String): CompanyInfoDto
}