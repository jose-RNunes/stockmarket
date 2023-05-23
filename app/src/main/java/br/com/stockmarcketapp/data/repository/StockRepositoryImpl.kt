package br.com.stockmarcketapp.data.repository

import br.com.stockmarcketapp.data.csv.CsvParser
import br.com.stockmarcketapp.data.local.CompanyEntity
import br.com.stockmarcketapp.data.local.StockDao
import br.com.stockmarcketapp.data.mapper.CompanyEntityMapper
import br.com.stockmarcketapp.data.mapper.CompanyInfoMapper
import br.com.stockmarcketapp.data.mapper.CompanyModelMapper
import br.com.stockmarcketapp.data.remote.StockApi
import br.com.stockmarcketapp.di.CompaniesParser
import br.com.stockmarcketapp.domain.model.CompanyInfoModel
import br.com.stockmarcketapp.domain.model.CompanyModel
import br.com.stockmarcketapp.domain.model.IntradayInfoModel
import br.com.stockmarcketapp.domain.repository.StockRepository
import br.com.stockmarcketapp.util.Resource
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val stockApi: StockApi,
    private val dao: StockDao,
    private val companyMapper: CompanyModelMapper,
    private val companyEntityMapper: CompanyEntityMapper,
    private val companyInfoMapper: CompanyInfoMapper,
    @CompaniesParser private val companiesParser: CsvParser<CompanyModel>,
    @br.com.stockmarcketapp.di.IntradayInfoParser private val intradayInfoParser: CsvParser<IntradayInfoModel>
) : StockRepository {

    override suspend fun getCompanyList(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyModel>>> {
        val stocks = fetchFromLocal(query)

        return if ((stocks.isEmpty() && query.isBlank()) || fetchFromRemote) {
            fetchFromApi()
        } else {
            flow { emit(Resource.Success(stocks)) }
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<Pair<CompanyInfoModel, List<IntradayInfoModel>>> {
        return coroutineScope {
            try {
                val companyInfo = async { getCompanyInfoInternal(symbol) }.await()
                val stocks = async { getIntradayInfo(symbol) }.await()

                Resource.Success(Pair(companyInfo, stocks))
            } catch (e: IOException) {
                Resource.Error("Couldn't load data")
            } catch (e: HttpException) {
                Resource.Error("Couldn't read data")
            }
        }
    }

    private suspend fun getIntradayInfo(symbol: String): List<IntradayInfoModel> {
        val result = stockApi.getIntradayInfo(symbol).byteStream()
        return intradayInfoParser.parser(result)
    }

    private suspend fun getCompanyInfoInternal(symbol: String): CompanyInfoModel {
        val result = stockApi.getCompanyInfo(symbol)
        return companyInfoMapper.map(result)
    }

    private suspend fun fetchFromLocal(query: String): List<CompanyModel> {
        val result = dao.searchCompany(query)
        return if (result.isNotEmpty()) {
            val resultMapper = result.map { companyMapper.map(it) }
            resultMapper
        } else {
            emptyList()
        }
    }

    private suspend fun fetchFromApi(): Flow<Resource<List<CompanyModel>>> {
        return flow {
            val remoteList = try {
                val result = stockApi.getListings().byteStream()
                companiesParser.parser(result)
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                emit(Resource.Error("Couldn't read data"))
                null
            }

            remoteList?.let { companies ->
                val result = insertAndGetCompanies(companies.map { companyEntityMapper.map(it) })
                emit(Resource.Success(result))
            }
        }
    }

    private suspend fun insertAndGetCompanies(
        companies: List<CompanyEntity>
    ): List<CompanyModel> {
        dao.delete()
        dao.insert(companies)
        return dao.searchCompany("").map { companyMapper.map(it) }
    }
}