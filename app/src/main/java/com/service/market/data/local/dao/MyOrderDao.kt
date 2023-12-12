package com.service.market.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.service.market.data.local.model.orders.MyOrders
import com.service.market.data.local.model.orders.Orders
import kotlinx.coroutines.flow.Flow

@Dao
interface MyOrderDao {

    //@Query("SELECT * FROM remont ORDER BY id DESC")
    //fun getAllRemont() : PagingSource<Int, Remont>
    @Query("SELECT * FROM my_orders ORDER BY id DESC")
    fun getAllMyOrders() : Flow<List<MyOrders>>

    @Query("SELECT * FROM my_orders WHERE id = :id ORDER BY id DESC")
    fun getMyOrder(id: Int): List<MyOrders>

    @Query("DELETE FROM my_orders")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<MyOrders>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orders: MyOrders)

    @Query("SELECT * FROM my_orders ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, MyOrders>

}