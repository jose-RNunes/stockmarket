package br.com.stockmarcketapp.presentation.company_detail

import br.com.stockmarcketapp.domain.model.CompanyInfoModel
import br.com.stockmarcketapp.domain.model.IntradayInfoModel

data class CompanyInfoState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val stockList: List<IntradayInfoModel> = emptyList(),
    val companyInfoModel: CompanyInfoModel? = null
) {
    fun showData() = isLoading.not() && error.isNullOrEmpty()
}