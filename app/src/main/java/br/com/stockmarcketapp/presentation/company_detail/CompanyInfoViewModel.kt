package br.com.stockmarcketapp.presentation.company_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.stockmarcketapp.domain.repository.StockRepository
import br.com.stockmarcketapp.util.onError
import br.com.stockmarcketapp.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val stockRepository: StockRepository
) : ViewModel() {

    private companion object {
        const val EXTRA_SYMBOL = "symbol"
    }

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>(EXTRA_SYMBOL) ?: return@launch
            state = state.copy(isLoading = true)

            stockRepository.getCompanyInfo(symbol)
                .onSuccess { data ->
                    state = CompanyInfoState(
                        companyInfoModel = data?.first,
                        stockList = data?.second ?: emptyList()
                    )
                }
                .onError { message ->
                    state = CompanyInfoState(error = message)
                }
        }
    }
}