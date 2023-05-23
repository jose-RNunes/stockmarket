package br.com.stockmarcketapp.presentation.companies

import br.com.stockmarcketapp.domain.model.CompanyModel

sealed class CompaniesEvent {
    object Init : CompaniesEvent()
    object Refreshing : CompaniesEvent()
    data class OnSearchQueryChanged(val query: String) : CompaniesEvent()
    data class OnCompanySelected(val companyModel: CompanyModel) : CompaniesEvent()
}
