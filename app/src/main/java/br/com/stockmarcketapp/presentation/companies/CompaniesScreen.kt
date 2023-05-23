package br.com.stockmarcketapp.presentation.companies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.stockmarcketapp.R
import br.com.stockmarcketapp.presentation.companies.CompaniesEvent.OnSearchQueryChanged
import br.com.stockmarcketapp.presentation.destinations.CompanyInfoScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompaniesScreen(
    navigator: DestinationsNavigator,
    viewModel: CompaniesViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )

    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            value = state.searchQuery,
            onValueChange = { query ->
                viewModel.handleEvent(OnSearchQueryChanged(query))
            },
            placeholder = { Text(text = stringResource(id = R.string.query_placeholder)) },
            maxLines = 1,
            singleLine = true
        )

        if (state.isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (state.isError) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Error to load data")
            }
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.handleEvent(CompaniesEvent.Refreshing) }) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (state.showData()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.companies.size) { item ->
                            val company = state.companies[item]
                            CompanyItem(
                                companyModel = company,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable {
                                       // viewModel.handleEvent(CompaniesEvent.OnCompanySelected(company))
                                        navigator.navigate(CompanyInfoScreenDestination(company.symbol))
                                    }
                            )

                            if (item < state.companies.size) {
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    color = MaterialTheme.colors.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}