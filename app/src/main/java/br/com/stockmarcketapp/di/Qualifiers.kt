package br.com.stockmarcketapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CompaniesParser

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IntradayInfoParser