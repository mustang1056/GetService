package com.service.market.data.repository

import android.content.Context
import com.service.market.data.local.dao.MyOrderDao
import com.service.market.data.local.model.orders.MyOrders
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class MyOrdersRepository @Inject constructor(
    private val myOrderDao: MyOrderDao,
    context: Context
) {
    init {
        Timber.d("Injection MainRepository")
    }

    suspend fun addMyOrder(myOrders: MyOrders) {
        myOrderDao.insert(myOrders)
    }

    fun getMyBlog(): Flow<List<MyOrders>> {
        return myOrderDao.getAllMyOrders()
    }
}