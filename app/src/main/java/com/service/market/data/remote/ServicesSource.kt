package com.service.market.data.remote

import com.service.market.data.local.model.service.ServiceList
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface ServicesSource {

    @GET("/service/api/service")
    suspend fun getAllServices(): ApiResponse<ServiceList>

}