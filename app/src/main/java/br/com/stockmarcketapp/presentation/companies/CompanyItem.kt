package br.com.stockmarcketapp.presentation.companies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import br.com.stockmarcketapp.domain.model.CompanyModel

@Composable
fun CompanyItem(
    companyModel: CompanyModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = companyModel.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = companyModel.exchange,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        
        Text(
            text = "(${companyModel.symbol})",
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colors.onBackground
        )
    }
}