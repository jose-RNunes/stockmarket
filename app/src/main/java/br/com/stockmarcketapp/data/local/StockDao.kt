package br.com.stockmarcketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companies: List<CompanyEntity>)

    @Query("DELETE FROM companyEntity")
    suspend fun delete()

    @Query(
        """
         SELECT * FROM companyEntity
         WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == symbol
        """
    )
    suspend fun searchCompany(query: String): List<CompanyEntity>
}