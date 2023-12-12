package com.service.market.data.repository

import androidx.annotation.WorkerThread

import com.service.market.data.local.model.auth.AuthData
import com.service.market.data.local.model.auth.RegistrData
import com.service.market.data.local.model.auth.Users
import com.service.market.data.local.model.orders.Orders
import com.service.market.data.remote.AuthService
import com.skydoves.sandwich.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
    ) {
    init {
        Timber.d("Injection MainRepository")
    }

    @WorkerThread
    fun authToken(onStart: () -> Unit,
                 onCompletion: () -> Unit,
                 onError: (String) -> Unit,
                 authData: AuthData
    )= flow {
        // request API network call asynchronously.
        authService.authToken(authData)
            // handle the case when the API request gets a success response.
            .suspendOnSuccess {
                //posterDao.insertAll(data.content)
                emit(data)
                onCompletion()
            }
            // handle the case when the API request gets an error response.
            // e.g. internal server error.
            .suspendOnError {
                onError(message())
                //Toast.makeText(this, "qwdqwd", Toast.LENGTH_SHORT).show()

            }
            // handle the case when the API request gets an exception response.
            // e.g. network connection error.
            .onException {
                onError(message())

            }
    }.onStart { onStart() }.onCompletion { }.flowOn(Dispatchers.IO)





    fun authRegist(onStart: () -> Unit,
                  onCompletion: () -> Unit,
                  onError: (String) -> Unit,
                  registrData: RegistrData
    )= flow {
            // request API network call asynchronously.
            authService.authRegister(registrData)
                // handle the case when the API request gets a success response.
                .suspendOnSuccess {
                    //posterDao.insertAll(data.content)
                    emit(data)
                    onCompletion()
                }
                // handle the case when the API request gets an error response.
                // e.g. internal server error.
                .suspendOnError {
                    onError(message())
                    //Toast.makeText(this, "qwdqwd", Toast.LENGTH_SHORT).show()

                }
                // handle the case when the API request gets an exception response.
                // e.g. network connection error.
                .onException {
                    onError(message())

                }
        }.onStart { onStart() }.onCompletion { }.flowOn(Dispatchers.IO)


    @WorkerThread
    fun fileUpload(onStart: () -> Unit,
                   onCompletion: () -> Unit,
                   onError: (String) -> Unit,
                   image: MultipartBody.Part
    )= flow {
        // request API network call asynchronously.
        authService.postImage(image)
            // handle the case when the API request gets a success response.
            .suspendOnSuccess {
                //posterDao.insertAll(data.content)
                emit(data.content)
                onCompletion()
            }
            // handle the case when the API request gets an error response.
            // e.g. internal server error.
            .onError {
                onError(message())

                //Toast.makeText(this, "qwdqwd", Toast.LENGTH_SHORT).show()

            }
            // handle the case when the API request gets an exception response.
            // e.g. network connection error.
            .onException {
                onError(message())

            }
    }.onStart { onStart() }.onCompletion { }.flowOn(Dispatchers.IO)


    @WorkerThread
    fun updateUser(onStart: () -> Unit,
                 onCompletion: () -> Unit,
                 onError: (String) -> Unit,
                 user: Users
    )= flow {
        // request API network call asynchronously.
        authService.updateUser(user)
            // handle the case when the API request gets a success response.
            .suspendOnSuccess {
                //posterDao.insertAll(data.content)
                emit(data.content)
                onCompletion()
            }
            // handle the case when the API request gets an error response.
            // e.g. internal server error.
            .onError {
                onError(message())

                //Toast.makeText(this, "qwdqwd", Toast.LENGTH_SHORT).show()

            }
            // handle the case when the API request gets an exception response.
            // e.g. network connection error.
            .onException {
                onError(message())

            }
    }.onStart { onStart() }.onCompletion { }.flowOn(Dispatchers.IO)

}