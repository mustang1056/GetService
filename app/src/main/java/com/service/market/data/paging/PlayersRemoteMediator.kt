package com.service.market.data.paging

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.auto.getremont.data.remote.OrderService
import com.service.market.data.local.AppDatabase
import com.service.market.data.local.model.orders.OrderList
import com.service.market.data.local.model.orders.Orders
import com.service.market.data.local.model.orders.RemoteKeys
import com.service.market.ui.order.getArrayList

import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class PlayersRemoteMediator(
    private val service: OrderService,
    private val db: AppDatabase,
    application: Application
) : RemoteMediator<Int, Orders>() {
    private val STARTING_PAGE_INDEX = 0
    private var service_ids = ""
    private val context = application

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Orders>): MediatorResult {


        val key = when (loadType) {
            LoadType.REFRESH -> {

                //return MediatorResult.Success(false)
                //if (db.orderDao().count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX


            var apiResponse : OrderList?

            try {
                var categoryList = getArrayList("saveCategory", context)

                if (categoryList != null) {
                    service_ids = categoryList.toString()
                }
            }catch (e:Exception){

            }

            println("www"+service_ids)

            if(service_ids.length > 2){
                apiResponse = service.getAllOrderByIds(page, service_ids).body()
            }
            else {
                apiResponse = service.getAllOrdersTest(page).body()
            }


            //val apiResponse = service.getAllOrdersTest(page).body()


            val playersList = apiResponse

            val endOfPaginationReached =
                apiResponse!!.last == true
                //apiResponse.pageNumber. .meta.next_page == null || apiResponse.meta.current_page == apiResponse.meta.total_pages

            db.withTransaction {
                val nextKey = page + 1

                if (loadType == LoadType.REFRESH) {
                    db.orderDao().deleteAll()
                    db.pageKeyDao().clearRemoteKeys()
                }

                db.pageKeyDao().insertKey(
                    RemoteKeys(
                        0,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )
                db.orderDao().insertAll(playersList!!.content)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return db.pageKeyDao().getKeys().firstOrNull()
    }


}