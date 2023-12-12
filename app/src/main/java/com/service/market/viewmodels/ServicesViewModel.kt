package com.service.market.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.service.market.data.local.AppDatabase
import com.service.market.data.local.dao.ServiceDao
import com.service.market.data.local.model.service.Services
import com.service.market.data.repository.ServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val serviceDao: ServiceDao,
    private val db: AppDatabase,
    private val servicesRepository: ServicesRepository
    ) : ViewModel() {

    lateinit var clickedItem: Services


    val servicesVal: Flow<List<Services>> =
        servicesRepository.getServices(
            onStart = { _isLoading.value = true },
            onCompletion = { _isLoading.value = false },
            onError = { Timber.d(it) },
            page = { 0 }
        ) as Flow<List<Services>>

    fun itemClicked(item: Services) {
        clickedItem = item
    }

    fun getServicesByIds(): Flow<List<Services>>{
        return servicesRepository.getServicesById(
            onStart = { _isLoading.value = true },
            onCompletion = { _isLoading.value = false },
            onError = { Timber.d(it) }
        ) as Flow<List<Services>>
    }

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading
}