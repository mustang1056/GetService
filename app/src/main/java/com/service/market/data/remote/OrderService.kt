package com.auto.getremont.data.remote


import com.service.market.data.local.model.orders.OrderList
import com.service.market.data.local.model.orders.Orders
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody


import retrofit2.Response
import retrofit2.http.*

interface OrderService {
    @GET("orders/api/orders")
    suspend fun getAllOrders(@Query("page") page: (Int)): ApiResponse<OrderList>

    @GET("orders/api/orders")
    suspend fun getAllOrdersTest(@Query("page") page: (Int)): Response<OrderList>

    @GET("orders/api/orders_service")
    suspend fun getAllOrderByIds(@Query("page") page:Int, @Query("ids") ids: String) : Response<OrderList>

    @GET("orders/api/order/{id}")
    suspend fun getOrder(@Path("id") id: Int): Response<Orders>

    @POST("orders/api/order")
    suspend fun addOrder(@Body blog: Orders):ApiResponse<OrderList>


}