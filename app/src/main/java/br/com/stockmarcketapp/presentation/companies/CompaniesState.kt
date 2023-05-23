package br.com.stockmarcketapp.presentation.companies

import br.com.stockmarcketapp.domain.model.CompanyModel

data class CompaniesState(
    val companies: List<CompanyModel> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
) {

    fun showData() = isLoading.not() && isRefreshing.not() && isError.not()
}