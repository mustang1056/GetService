package com.example.getdriver.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.auto.ingram.ui.navigation.BottomNavItem
import com.service.market.data.local.model.orders.MyOrders
import com.service.market.data.local.model.orders.Orders
import com.service.market.data.repository.MyOrdersRepository

import com.service.market.data.repository.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OrdersFormViewModel @Inject constructor(
    mainRepository: OrdersRepository,
    myOrdersRepository: MyOrdersRepository
) : ViewModel() {

    private var repository = mainRepository

    private var myOrdersRepository = myOrdersRepository

    val _loginState = MutableStateFlow<LoginUIState>(LoginUIState.Empty)
    private val loginUIState: StateFlow<LoginUIState> = _loginState

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    private val isLoading: State<Boolean> get() = _isLoading

    //simulate login process
    suspend fun addOrder(title: String, comment: String, cost: Double, category_id: Int, phone : String, service_title : String, avatar_image: String, user_id: Int): Flow<List<Orders>> {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        val currentDate = sdf.format(Date())

        val orders = Orders(
            title =title,
            comment = comment,
            cost =cost,
            date_time =currentDate,
            service_id = category_id,
            sub_service_id = 1,
            user_id = user_id,
            phone_number = phone,
            service_title = service_title,
            avatar_image = avatar_image
        )

        val my_orders = MyOrders(
            title =title,
            comment = comment,
            cost =cost,
            date_time =currentDate,
            service_id = category_id,
            sub_service_id = 1,
            user_id = 1,
            phone_number = phone,
            service_title = service_title
        )

        myOrdersRepository.addMyOrder(my_orders)

        return repository.addOrder(
            onStart = {_loginState.value = LoginUIState.Loading },
            onCompletion = { _loginState.value =LoginUIState.Success },
            onError = { _loginState.value =LoginUIState.Error(it) },
            orders
        )
    }


    // login ui states
    sealed class LoginUIState {
        object Success : LoginUIState()
        data class Error(val message: String) : LoginUIState()
        object Loading : LoginUIState()
        object Empty : LoginUIState()
    }


}
