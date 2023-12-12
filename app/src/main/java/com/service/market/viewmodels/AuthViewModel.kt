package com.service.market.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.getdriver.viewmodels.OrdersFormViewModel
import com.service.market.data.local.model.auth.AuthData
import com.service.market.data.local.model.auth.AuthResponseData
import com.service.market.data.local.model.auth.RegistrData
import com.service.market.data.local.model.auth.Users
import com.service.market.data.local.model.orders.MyOrders
import com.service.market.data.local.model.orders.Orders
import com.service.market.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    authRepository: AuthRepository
):ViewModel(){

    private var repository = authRepository

    val _loginState = MutableStateFlow<LoginUIState>(LoginUIState.Empty)
    val loginUIState: StateFlow<LoginUIState> = _loginState

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    private val isLoading: State<Boolean> get() = _isLoading

    //simulate login process
    suspend fun authToken(email: String, password: String): Flow<AuthResponseData> {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        val currentDate = sdf.format(Date())

        val authData = AuthData(
                email.trim(),
                password.trim(),
                ""
        )

        return repository.authToken(
            onStart = {_loginState.value = LoginUIState.Loading },
            onCompletion = { _loginState.value =LoginUIState.Success },
            onError = {
                _loginState.value =LoginUIState.Error(it) },
            authData
        )
    }

    //simulate login process
    suspend fun authRegistr(email: String, password: String, phone: String): Flow<AuthResponseData> {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        val currentDate = sdf.format(Date())

        val registrData = RegistrData(
            email.trim(),
            password.trim(),
            phone.trim()
        )

        return repository.authRegist(
            onStart = {_loginState.value = LoginUIState.Loading },
            onCompletion = { _loginState.value =LoginUIState.Success },
            onError = {
                _loginState.value =LoginUIState.Error(it) },
            registrData
        )
    }

    suspend fun addImage(image: MultipartBody.Part ): Flow<List<Orders>>{
        return repository.fileUpload(onStart = {_loginState.value = LoginUIState.Loading },
            onCompletion = { _loginState.value =LoginUIState.Success },
            onError = { _loginState.value =LoginUIState.Error(it) },image)
    }


    suspend fun updateUser(id: Int, name: String, email: String, password: String, phone_number: String): Flow<List<Orders>> {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        val currentDate = sdf.format(Date())

        val users = Users(
            id = id,
            name =name,
            email = email,
            password = password,
            phone_number = phone_number
        )

        return repository.updateUser(
            onStart = {_loginState.value = AuthViewModel.LoginUIState.Loading },
            onCompletion = { _loginState.value = AuthViewModel.LoginUIState.Success },
            onError = { _loginState.value = AuthViewModel.LoginUIState.Error(it) },
            users
        )
    }


    fun getLoginState(): StateFlow<LoginUIState> {
        return loginUIState
    }


    // login ui states
    sealed class LoginUIState {
        object Success : LoginUIState()
        data class Error(val message: String) : LoginUIState()
        object Loading : LoginUIState()
        object Empty : LoginUIState()
    }

}