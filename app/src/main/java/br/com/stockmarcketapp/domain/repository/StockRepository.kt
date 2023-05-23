package br.com.stockmarcketapp.domain.repository

import br.com.stockmarcketapp.domain.model.CompanyInfoModel
import br.com.stockmarcketapp.domain.model.CompanyModel
import br.com.stockmarcketapp.domain.model.IntradayInfoModel
import br.com.stockmarcketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyList(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyModel>>>

    suspend fun getCompanyInfo(symbol: String): Resource<Pair<CompanyInfoModel, List<IntradayInfoModel>>>
}