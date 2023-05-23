package br.com.stockmarcketapp.di

import android.app.Application
import androidx.room.Room
import br.com.stockmarcketapp.data.local.StockDao
import br.com.stockmarcketapp.data.local.StockDatabase
import br.com.stockmarcketapp.data.mapper.CompanyEntityMapper
import br.com.stockmarcketapp.data.mapper.CompanyInfoMapper
import br.com.stockmarcketapp.data.mapper.CompanyModelMapper
import br.com.stockmarcketapp.data.mapper.IntradayInfoMapper
import br.com.stockmarcketapp.data.remote.AuthenticateInterceptor
import br.com.stockmarcketapp.data.remote.RetrofitConfig
import br.com.stockmarcketapp.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://alphavantage.co"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(AuthenticateInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideStockApi(): StockApi {
        return RetrofitConfig.create()
    }

    @Provides
    @Singleton
    fun provideStockDatabase(application: Application): StockDatabase {
        return Room.databaseBuilder(
            application,
            StockDatabase::class.java,
            "stock_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideStockDao(database: StockDatabase): StockDao {
        return database.dao
    }
}