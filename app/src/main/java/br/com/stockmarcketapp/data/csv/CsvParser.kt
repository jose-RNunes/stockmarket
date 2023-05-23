package br.com.stockmarcketapp.data.csv

import java.io.InputStream

interface CsvParser<T> {
    suspend fun parser(stream: InputStream): List<T>
}