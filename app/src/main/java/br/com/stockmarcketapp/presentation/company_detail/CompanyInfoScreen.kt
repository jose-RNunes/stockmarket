package br.com.stockmarcketapp.presentation.company_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.stockmarcketapp.ui.theme.DarkBlue
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun CompanyInfoScreen(
    symbol: String,
    viewModel: CompanyInfoViewModel = hiltViewModel()
) {

    val state = viewModel.state

    if (state.showData()) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(DarkBlue)
                .padding(16.dp)
        ) {
            val companyInfo = state.companyInfoModel ?: return

            Text(
                text = companyInfo.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = companyInfo.symbol,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Industry: ${companyInfo.industry}",
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Country: ${companyInfo.country}",
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = companyInfo.description,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.stockList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Market Summary")

                Spacer(modifier = Modifier.height(32.dp))

                StockChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .align(Alignment.CenterHorizontally),
                    stocks = state.stockList
                )
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if(state.isLoading) {
            CircularProgressIndicator()
        }

        if(state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colors.error
            )
        }
    }
}