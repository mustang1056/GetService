package com.auto.getremont.data.remote

import com.service.market.data.local.model.orders.Orders
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    private val remontService: OrderService
): BaseDataSource() {

    suspend fun getOrders(page: Int) = remontService.getAllOrders(page)
    suspend fun getOrder(id: Int) = getResult { remontService.getOrder(id) }
    suspend fun addOrder(remont: Orders) = remontService.addOrder(remont)

}