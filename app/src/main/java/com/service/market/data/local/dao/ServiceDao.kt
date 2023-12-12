package com.service.market.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.service.market.data.local.model.service.Services

@Dao
interface ServiceDao {
    //@Query("SELECT * FROM remont ORDER BY id DESC")
    //fun getAllRemont() : PagingSource<Int, Remont>
    @Query("SELECT * FROM services ORDER BY id")
    fun getAllServices() : List<Services>

    @Query("SELECT * FROM services WHERE id = :id ORDER BY id")
    fun getServices(id: Int): List<Services>

    @Query("SELECT * FROM services WHERE id IN (:userIds) ORDER BY id")
    fun getServicesById(userIds: List<Int>): List<Services>

    @Query("DELETE FROM services")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(services: List<Services>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(services: Services)
}