package com.example.getdriver.ui.order

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.getdriver.viewmodels.OrdersViewModel

import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.service.market.data.local.model.orders.Orders
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.draw.paint
import androidx.compose.ui.res.painterResource
import com.service.market.R
import com.service.market.data.local.model.service.Services
import com.service.market.viewmodels.ServicesViewModel


import kotlinx.coroutines.delay

private val SaveMap = mutableMapOf<String, KeyParams>()
private var isRefreshing = false

private data class KeyParams(
    val params: String = "",
    val index: Int,
    val scrollOffset: Int
)


@ExperimentalPagingApi
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalFoundationApi
@Composable
fun MainList(navController: NavController, viewModel : OrdersViewModel, viewModelServices: ServicesViewModel, lazyMovieItems: LazyPagingItems<Orders>
){



    //val posters: List<Remont> by mainViewModel.posterList.collectAsState(initial = listOf())

    RemontList(navController = navController,movieList = lazyMovieItems,
        onItemClicked = viewModel::itemClicked, servicesViewModel = viewModelServices)
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


fun sendMsg(refresh: Boolean){
    isRefreshing = refresh
}


@Composable
fun rememberForeverLazyListState(
    key: String,
    params: String = "",
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyListState {
    val scrollState = rememberSaveable(saver = LazyListState.Saver) {
        var savedValue = SaveMap[key]
        if (savedValue?.params != params) savedValue = null
        val savedIndex = savedValue?.index ?: initialFirstVisibleItemIndex
        val savedOffset = savedValue?.scrollOffset ?: initialFirstVisibleItemScrollOffset
        LazyListState(
            savedIndex,
            savedOffset
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            val lastIndex = scrollState.firstVisibleItemIndex
            val lastOffset = scrollState.firstVisibleItemScrollOffset
            SaveMap[key] = KeyParams(params, lastIndex, lastOffset)
        }
    }
    return scrollState
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FilterFlip(itemsList: List<Services>) {

    //val itemsList = listOf(list_services)

    val contextForToast = LocalContext.current.applicationContext

    var selectedItem by remember {
        mutableStateOf(itemsList[0]) // initially, first item is selected
    }

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(itemsList) { item ->
            FilterChip(
                modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                selected = (item == selectedItem),
                onClick = {
                    selectedItem = item
                    //Toast.makeText(contextForToast, selectedItem, Toast.LENGTH_SHORT).show()
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}

@ExperimentalPagingApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RemontList(
    navController: NavController,
    movieList:LazyPagingItems<Orders>,
    servicesViewModel: ServicesViewModel,
    onItemClicked:(item: Orders) ->Unit
) {
    var listState = rememberLazyListState()
    val Red = Color(red = 35, green = 61, blue = 83)

    val contextForToast = LocalContext.current.applicationContext
    val sharedPreference = contextForToast.getSharedPreferences(
        "back_stack",
        Context.MODE_PRIVATE
    )

    val is_back =  sharedPreference.getBoolean("back",false)

    if(is_back == false){
        isRefreshing = true
    }

    var editor = sharedPreference.edit()
    editor.putBoolean("back", false)
    editor.commit()

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
            movieList.refresh()
            refreshing = false
        }
    }

    ComposableLifecycle { source, event ->
        if (event == Lifecycle.Event.ON_RESUME) {

            if(isRefreshing == true) {
                movieList.refresh()
                isRefreshing = false
            }
        }
    }

    val serv = servicesViewModel.getServicesByIds().collectAsState(initial = listOf())

    //val scrollState = rememberForeverScrollState("history_screen")




    Column{
        if(serv.value.size > 0) {
            FilterFlip(serv.value)
        }

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = { refreshing = true },
        ) {

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 16.dp),

            // New vertical spacing
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = rememberForeverLazyListState(key = "Overview")
            ) {


                items(items = movieList, itemContent = { item ->

                    if (item != null) {
                        ListViewItem(navController = navController, orderItem = item, onItemClicked)
                    }

                })

                movieList.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { /*LoadingView(modifier = Modifier.fillParentMaxSize())*/ }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { /*LoadingItem()*/ }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val e = movieList.loadState.refresh as LoadState.Error
                            item {
                                /*ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )*/
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            val e = movieList.loadState.append as LoadState.Error
                            item {
                                /*ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )*/
                            }
                        }
                    }
                }
            }


        }

    }


    /**
     * Static field, contains all scroll values
     */

    @Composable
    fun LoadingView(modifier: Any) {

    }




}


//private val SaveMap = mutableMapOf<String, ScrollKeyParams>()

private data class ScrollKeyParams(
    val value: Int
)

/**
 * Save scroll state on all time.
 * @param key value for comparing screen
 * @param initial see [ScrollState.value]
 */

/*
@Composable
fun rememberForeverScrollState(
    key: String,
    initial: Int = 0
): ScrollState {
    val scrollState = rememberSaveable(saver = ScrollState.Saver) {
        val scrollValue: Int = SaveMap[key]?.value ?: initial
        SaveMap[key] = ScrollKeyParams(scrollValue)
        return@rememberSaveable ScrollState(scrollValue)
    }
    DisposableEffect(Unit) {
        onDispose {
            SaveMap[key] = ScrollKeyParams(scrollState.value)
        }
    }
    return scrollState
}*/