package br.com.stockmarcketapp.di

import br.com.stockmarcketapp.data.csv.CompaniesParser
import br.com.stockmarcketapp.data.csv.CsvParser
import br.com.stockmarcketapp.data.csv.IntradayInfoParser
import br.com.stockmarcketapp.data.repository.StockRepositoryImpl
import br.com.stockmarcketapp.domain.model.CompanyModel
import br.com.stockmarcketapp.domain.model.IntradayInfoModel
import br.com.stockmarcketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @br.com.stockmarcketapp.di.CompaniesParser
    @Binds
    @Singleton
    abstract fun bindCompaniesParser(companiesParser: CompaniesParser): CsvParser<CompanyModel>

    @br.com.stockmarcketapp.di.IntradayInfoParser
    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(intradayInfoParser: IntradayInfoParser): CsvParser<IntradayInfoModel>

    @Binds
    @Singleton
    abstract fun bindStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository
}