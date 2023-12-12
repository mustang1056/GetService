package com.service.market.ui.my_orders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.paging.ExperimentalPagingApi

import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.service.market.data.local.model.orders.MyOrders
import com.service.market.viewmodels.MyOrderViewModel
import kotlinx.coroutines.delay


@ExperimentalPagingApi
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalFoundationApi
@Composable
fun MyOrderList(navController: NavController, viewModel : MyOrderViewModel
){


    /*
    val lazyMovieItems: LazyPagingItems<Blogs> = viewModel.blogs.collectAsState(initial = )
*/

    MyOrderList(navController = navController,viewModel = viewModel,
        onItemClicked = viewModel::itemClicked)
}

@Composable
fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@ExperimentalPagingApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MyOrderList(
    navController: NavController,
    viewModel: MyOrderViewModel,
    onItemClicked:(item: MyOrders) ->Unit
) {
    var listState = rememberLazyListState()
    val Red = Color(red = 35, green = 61, blue = 83)

    val posters by viewModel.myOrders.collectAsState(initial = listOf())

    /*
    val viewModel = hiltViewModel<RemontViewModel>()

    val lazyMovieItems: LazyPagingItems<Remont> = viewModel.movies.collectAsLazyPagingItems()
*/

    /*
    LazyColumn(state = listState) {
        itemsIndexed(movieList){index, item ->
            ListViewItem(navController = navController,remontItem = item,onItemClicked)
        }
    }*/

    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(3000)
            //movieList.refresh()
            refreshing = false
        }
    }

    ComposableLifecycle { source, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            //movieList.refresh()
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refreshing = true },
    ) {

        LazyColumn(contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp),
            // New vertical spacing
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            println(posters.size)

            items(posters) { item ->

                if (item != null) {
                    ListMyOrderViewItem(navController = navController, remontItem = item, onItemClicked)
                }

            }

        }
    }


    @Composable
    fun LoadingView(modifier: Any) {

    }
}