package br.com.stockmarcketapp.data.mapper

import br.com.stockmarcketapp.data.remote.dto.IntradayInfoDto
import br.com.stockmarcketapp.domain.model.IntradayInfoModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class IntradayInfoMapper @Inject constructor(): Mapper<IntradayInfoDto, IntradayInfoModel> {

    private companion object {
        const val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
    }

    override fun map(input: IntradayInfoDto): IntradayInfoModel {
        return IntradayInfoModel(
            date = input.timestamp.convertTimestampToLocalDate(),
            close = input.close
        )
    }

    private fun String.convertTimestampToLocalDate(): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.getDefault())
        return LocalDateTime.parse(this, formatter)
    }
}