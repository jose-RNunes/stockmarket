package br.com.stockmarcketapp.data.mapper

internal interface Mapper<I, O> {
    fun map(input: I): O
}