package com.example.getdriver.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.auto.getremont.data.remote.OrderService
import com.example.getdriver.data.local.dao.OrderDao
import com.service.market.data.local.AppDatabase
import com.service.market.data.local.model.orders.Orders
import com.service.market.data.paging.PlayersRemoteMediator


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderService: OrderService,
    private val orderDao: OrderDao,
    private val db: AppDatabase,
    application: Application
) : ViewModel() {
    lateinit var clickedItem: Orders



    @ExperimentalPagingApi
    val orders: Flow<PagingData<Orders>> =  Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = PlayersRemoteMediator(
            orderService,
            db,
            application
        ),
        pagingSourceFactory = { orderDao.pagingSource()}).flow



    fun itemClicked(item: Orders) {
        clickedItem = item
    }
}