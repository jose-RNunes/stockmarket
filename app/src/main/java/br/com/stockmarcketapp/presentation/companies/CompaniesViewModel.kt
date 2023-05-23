package br.com.stockmarcketapp.presentation.companies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.stockmarcketapp.domain.model.CompanyModel
import br.com.stockmarcketapp.domain.repository.StockRepository
import br.com.stockmarcketapp.presentation.companies.CompaniesEvent.OnCompanySelected
import br.com.stockmarcketapp.presentation.companies.CompaniesEvent.OnSearchQueryChanged
import br.com.stockmarcketapp.presentation.companies.CompaniesEvent.Refreshing
import br.com.stockmarcketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class CompaniesViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {

    private companion object {
        const val DEBOUNCE_DELAY = 500L
    }

    private var searchJob: Job? = null

    var state by mutableStateOf(CompaniesState())

    init {
        getCompanies()
    }

    fun handleEvent(event: CompaniesEvent) {
        when (event) {
            is OnSearchQueryChanged -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(DEBOUNCE_DELAY)
                    getCompanies()
                }
            }
            is OnCompanySelected -> {}
            is Refreshing -> onRefreshing()
        }
    }

    private fun getCompanies(
        fetchFromRemote: Boolean = false,
        query: String = state.searchQuery.lowercase()
    ) {
        viewModelScope.launch {
            state = state.copy(isLoading = fetchFromRemote && state.isRefreshing.not())
            stockRepository.getCompanyList(fetchFromRemote, query)
                .collect { result ->
                    state = when (result) {
                        is Resource.Success -> {
                            state.copy(
                                isRefreshing = false,
                                isLoading = false,
                                companies = result.data ?: emptyList()
                            )
                        }

                        is Resource.Error -> {
                            state.copy(
                                isRefreshing = false,
                                isLoading = false,
                                isError = true
                            )
                        }
                    }
                }
        }
    }

    private fun onCompanySelected(companyModel: CompanyModel) {

    }

    private fun onRefreshing() {
        state = state.copy(isRefreshing = true)
        getCompanies(fetchFromRemote = true)
    }
}