package com.service.market.viewmodels

import androidx.lifecycle.ViewModel
import com.service.market.data.local.AppDatabase
import com.service.market.data.local.model.orders.MyOrders
import com.service.market.data.repository.MyOrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class MyOrderViewModel @Inject constructor(
    private val db: AppDatabase,
    private val myOrdersRepository: MyOrdersRepository
) : ViewModel() {

    lateinit var clickedItem: MyOrders


    val myOrders: Flow<List<MyOrders>> = myOrdersRepository.getMyBlog()

    fun itemClicked(item: MyOrders) {
        clickedItem = item
    }
}