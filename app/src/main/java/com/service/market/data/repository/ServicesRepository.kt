package com.service.market.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.service.market.data.local.dao.ServiceDao
import com.service.market.data.local.model.service.Services
import com.service.market.data.remote.ServicesSource
import com.service.market.ui.order.getArrayList
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class ServicesRepository @Inject constructor(
    private val services: ServicesSource,
    private val serviceDao: ServiceDao,
    context: Context
) {
    init {
        Timber.d("Injection ServicesRepository")
    }

    private val context = context

    @WorkerThread
    fun getServices(
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit,
        page: (Int) -> Unit
    ) = flow {
        // request API network call asynchronously.
        services.getAllServices()
            // handle the case when the API request gets a success response.
            .suspendOnSuccess {
                serviceDao.deleteAll()
                serviceDao.insertAll(data.content)
                val posters: List<Services> = serviceDao.getAllServices()
                emit(posters)
            }
            // handle the case when the API request gets an error response.
            // e.g. internal server error.
            .onError {
                onError(message())
            }
            // handle the case when the API request gets an exception response.
            // e.g. network connection error.
            .onException {
                onError(message())
            }

    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)

    fun getServicesById(  onStart: () -> Unit,
                          onCompletion: () -> Unit,
                          onError: (String) -> Unit)
    = flow {

        var service_ids: String = ""
        var result =listOf<Int>()
        try {
            var categoryList = getArrayList("saveCategory", context)

            if (categoryList != null) {
                service_ids = categoryList.toString()
                result = service_ids.removeSurrounding("[","]").replace(" ","").split(",").map { it.toInt() }
            }

        } catch (e: Exception) {

        }
        println("fwewfwefwefwef"+service_ids)

        val list = serviceDao.getServicesById(result)


        emit(list)
    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)

}