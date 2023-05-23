package br.com.stockmarcketapp.data.mapper

import br.com.stockmarcketapp.data.local.CompanyEntity
import br.com.stockmarcketapp.domain.model.CompanyModel
import javax.inject.Inject

class CompanyEntityMapper @Inject constructor() : Mapper<CompanyModel, CompanyEntity> {

    override fun map(input: CompanyModel): CompanyEntity {
        return CompanyEntity(
            name = input.name,
            symbol = input.symbol,
            exchange = input.exchange
        )
    }
}