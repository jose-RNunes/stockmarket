package br.com.stockmarcketapp.data.csv

import br.com.stockmarcketapp.domain.model.CompanyModel
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class CompaniesParser @Inject constructor() : CsvParser<CompanyModel> {

    private companion object {
        const val NUM_DROP_LINES = 1
    }

    override suspend fun parser(stream: InputStream): List<CompanyModel> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return suspendCoroutine { continuation ->
            try {
                val companies = csvReader
                    .readAll()
                    .drop(NUM_DROP_LINES)
                    .mapNotNull { line ->
                        CompanyModel(
                            symbol = line.getOrNull(0) ?: return@mapNotNull null,
                            name = line.getOrNull(1) ?: return@mapNotNull null,
                            exchange = line.getOrNull(2) ?: return@mapNotNull null
                        )
                    }
                continuation.resume(companies).also {
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