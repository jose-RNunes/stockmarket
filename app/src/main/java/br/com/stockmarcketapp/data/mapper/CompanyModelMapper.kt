package br.com.stockmarcketapp.data.mapper

import br.com.stockmarcketapp.data.local.CompanyEntity
import br.com.stockmarcketapp.domain.model.CompanyModel
import javax.inject.Inject

class CompanyModelMapper @Inject constructor() : Mapper<CompanyEntity, CompanyModel> {

    override fun map(input: CompanyEntity): CompanyModel {
        return CompanyModel(
            name = input.name,
            symbol = input.symbol,
            exchange = input.exchange
        )
    }
}