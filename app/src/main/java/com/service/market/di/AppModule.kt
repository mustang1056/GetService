package com.service.market.di

import android.content.Context
import com.auto.getremont.data.remote.OrderRemoteDataSource
import com.auto.getremont.data.remote.OrderService
import com.example.getdriver.data.local.dao.OrderDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.service.market.data.local.AppDatabase
import com.service.market.data.local.dao.MyOrderDao
import com.service.market.data.local.dao.ServiceDao
import com.service.market.data.remote.AuthService
import com.service.market.data.remote.ServicesSource
import com.service.market.data.repository.AuthRepository
import com.service.market.data.repository.MyOrdersRepository
import com.service.market.data.repository.OrdersRepository
import com.service.market.data.repository.ServicesRepository
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
        .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()


    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.38:8082/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCharacterService(retrofit: Retrofit): OrderService = retrofit.create(OrderService::class.java)

    @Provides
    fun provideService(retrofit: Retrofit): ServicesSource = retrofit.create(ServicesSource::class.java)

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)


    @Singleton
    @Provides
    fun provideCharacterRemoteDataSource(blogsService: OrderService) = OrderRemoteDataSource(blogsService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.orderDao()

    @Singleton
    @Provides
    fun provideServiceDao(db: AppDatabase) = db.serviceDao()

    @Singleton
    @Provides
    fun provideMyOrderDao(db: AppDatabase) = db.myOrderDao()


    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: OrderService,
                          localDataSource: OrderDao
    ) = OrdersRepository(remoteDataSource,localDataSource)

    @Singleton
    @Provides
    fun provideServiceRepository(remoteDataSource: ServicesSource,
                          localDataSource: ServiceDao,
                                 @ApplicationContext appContext: Context
    ) = ServicesRepository(remoteDataSource,localDataSource, appContext)

    @Singleton
    @Provides
    fun provideAuthRepository(authService: AuthService)
    = AuthRepository(authService)

    @Singleton
    @Provides
    fun provideMyOrderRepository(myOrderDao: MyOrderDao,
                               @ApplicationContext appContext: Context)
            = MyOrdersRepository(myOrderDao, appContext)

}