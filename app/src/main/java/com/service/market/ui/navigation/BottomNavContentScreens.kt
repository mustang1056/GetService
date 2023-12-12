package com.auto.ingram.ui.navigation

import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.getdriver.ui.order.MainList
import com.example.getdriver.ui.order_form.*
import com.example.getdriver.viewmodels.OrdersViewModel
import com.service.market.data.local.model.orders.Orders
import com.service.market.ui.auth.AuthScreen
import com.service.market.ui.auth.RegistrScreen
import com.service.market.ui.order_form.CategoryList
import com.service.market.viewmodels.ServicesViewModel


@OptIn(ExperimentalPagingApi::class, ExperimentalFoundationApi::class)
@Composable
fun BlogScreen(navController : NavController, viewModel: OrdersViewModel, servicesViewModel: ServicesViewModel, lazyMovieItems: LazyPagingItems<Orders>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Surface(color = MaterialTheme.colors.background) {
            MainList(navController = navController, viewModel, servicesViewModel, lazyMovieItems)
        }
    }
}

@Composable
fun NetworkScreen(navController: NavController,servicesViewModel: ServicesViewModel ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
            CategoryList(navController = navController, servicesViewModel = servicesViewModel)
    }
}

@OptIn(ExperimentalPagingApi::class, ExperimentalFoundationApi::class)
@Composable
fun AddPostScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
       // MyUI(navController)
        RegistrScreen(navController)
    }
}



