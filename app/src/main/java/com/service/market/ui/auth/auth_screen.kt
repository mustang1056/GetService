package com.service.market.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.auto.ingram.ui.navigation.BottomNavItem
import com.service.market.ui.tools.BasicBarDetail
import com.service.market.viewmodels.AuthViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AuthScreen(navController : NavController) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }


    var showDialog by remember { mutableStateOf(false) }

    val contextForToast = LocalContext.current.applicationContext

    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<AuthViewModel>()

    val loginUIState = viewModel.getLoginState()

    var showPassword by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = { BasicBarDetail(navController = navController) }) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    title = {
                        Text(text = "Внимание!")
                    },
                    text = {
                        Text("Неверный e-mail или пароль.")
                    },
                    confirmButton = {

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { showDialog = false



                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }


            TextField(
                value = email,
                onValueChange = { newText ->
                    email = newText.toString()
                },
                label = { Text(text = "E-mail") },
                placeholder = { Text(text = "E-mail") },
                isError = isEmailValid,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                trailingIcon = {
                    if(isEmailValid == true){
                        Icon(Icons.Filled.Warning, "error has occurred")
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon"
                    )
                }
            )

            if (isEmailValid) {
                Text(
                    text = "Введите e-mail",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }



            TextField(
                value = password,
                onValueChange = { newText ->
                    password = newText.toString()
                },
                label = { Text(text = "Пароль") },
                placeholder = { Text(text = "Пароль") },
                isError = isPasswordValid,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                trailingIcon = {
                    if(isEmailValid == true){
                        Icon(Icons.Filled.Warning, "error has occurred")
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Lock Icon"
                    )
                },
                visualTransformation = (if (showPassword) VisualTransformation.None else PasswordVisualTransformation()) as VisualTransformation
            )

            if (isPasswordValid) {
                Text(
                    text = "Введите пароль",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }

            Button(
                onClick = {


                    var is_error = false

                    if (email.isEmpty()) {
                        isEmailValid = true
                        is_error = true
                    } else {
                        isEmailValid = false
                    }

                    if (password.isEmpty()) {
                        isPasswordValid = true
                        is_error = true
                    } else {
                        isPasswordValid = false
                    }

                    if(is_error == false) {
                        coroutineScope.launch {
                            viewModel.authToken(
                                email, password
                            ).collect {
                                val token = it.token
                                val user_id = it.user_id
                                val phone = it.phone_number
                                val email = it.email

                                val sharedPreference = contextForToast.getSharedPreferences(
                                    "account_info",
                                    Context.MODE_PRIVATE
                                )
                                var editor = sharedPreference.edit()
                                editor.putString("token", token)
                                editor.putInt("user_id", user_id)
                                editor.putString("phone", phone)
                                editor.putString("password", password)
                                editor.putBoolean("is_auth", true)
                                editor.putString("email", email)
                                editor.commit()
                            }

                            viewModel._loginState.collect { loginUIState ->
                                when (loginUIState) {
                                    is AuthViewModel.LoginUIState.Success -> {
                                        navController.navigate(BottomNavItem.RemontList.screen_route)
                                    }
                                    is AuthViewModel.LoginUIState.Loading -> {}
                                    is AuthViewModel.LoginUIState.Error -> {
                                        showDialog = true

                                    }
                                    is AuthViewModel.LoginUIState.Empty -> {

                                    }

                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(size = 22.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 15.dp)
                    .padding(30.dp, 0.dp),
            ) {
                Text(
                    text = "Авторизоваться",
                    fontSize = 16.sp
                )

            }

            Button(
                onClick = {
                    navController.navigate(BottomNavItem.RegistrForm.screen_route)
                },
                shape = RoundedCornerShape(size = 22.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 15.dp)
                    .padding(30.dp, 0.dp),
            ) {
                Text(
                    text = "Регистрация",
                    fontSize = 16.sp
                )

            }
        }
    }
}


