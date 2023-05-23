package br.com.stockmarcketapp.data.mapper

import br.com.stockmarcketapp.data.remote.dto.CompanyInfoDto
import br.com.stockmarcketapp.domain.model.CompanyInfoModel
import javax.inject.Inject

class CompanyInfoMapper @Inject constructor() : Mapper<CompanyInfoDto, CompanyInfoModel> {
    override fun map(input: CompanyInfoDto): CompanyInfoModel {
        return CompanyInfoModel(
            symbol = input.symbol,
            name = input.name,
            description = input.description,
            country = input.country,
            industry = input.industry
        )
    }
}