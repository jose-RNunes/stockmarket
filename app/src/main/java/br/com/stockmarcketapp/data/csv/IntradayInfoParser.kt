package br.com.stockmarcketapp.data.csv

import br.com.stockmarcketapp.data.mapper.IntradayInfoMapper
import br.com.stockmarcketapp.data.remote.dto.IntradayInfoDto
import br.com.stockmarcketapp.domain.model.IntradayInfoModel
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class IntradayInfoParser @Inject constructor(
    private val intradayInfoMapper: IntradayInfoMapper
) : CsvParser<IntradayInfoModel> {

    private companion object {
        const val NUM_DROP_LINES = 1
        const val MINUS_DAY = 1L
    }

    override suspend fun parser(stream: InputStream): List<IntradayInfoModel> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return suspendCoroutine { continuation ->
            try {
                val instradaysInfo = csvReader
                    .readAll()
                    .drop(NUM_DROP_LINES)
                    .mapNotNull { line ->
                        val intradayInfoDto = IntradayInfoDto(
                            timestamp = line.getOrNull(0) ?: return@mapNotNull null,
                            close = line.getOrNull(4)?.toDoubleOrNull() ?: return@mapNotNull null
                        )
                        intradayInfoMapper.map(intradayInfoDto)
                    }
                    .filter {
                        it.date.dayOfMonth == LocalDateTime.now().minusDays(MINUS_DAY).dayOfMonth
                    }.sortedBy {
                        it.date.hour
                    }
                continuation.resume(instradaysInfo).also {
                    csvReader.close()
                }
            } catch (e: IOException) {
                continuation.resumeWithException(e)
            } catch (c: CsvException) {
                continuation.resumeWithException(c)
            }
        }
    }
}