package com.service.market.data.remote;

import com.service.market.data.local.model.auth.AuthData;
import com.service.market.data.local.model.auth.AuthResponseData
import com.service.market.data.local.model.auth.RegistrData
import com.service.market.data.local.model.auth.Users
import com.service.market.data.local.model.orders.OrderList
import com.skydoves.sandwich.ApiResponse;
import okhttp3.MultipartBody
import retrofit2.http.*

interface AuthService {

    @POST("auth/token")
    suspend fun authToken(@Body authData:AuthData):ApiResponse<AuthResponseData>

    @POST("auth/register")
    suspend fun authRegister(@Body authData: RegistrData):ApiResponse<AuthResponseData>

    @Multipart
    @POST("auth/upload")
    suspend fun postImage(@Part image: MultipartBody.Part): ApiResponse<OrderList>

    @POST("auth/update")
    suspend fun updateUser(@Body users: Users): ApiResponse<OrderList>

}
