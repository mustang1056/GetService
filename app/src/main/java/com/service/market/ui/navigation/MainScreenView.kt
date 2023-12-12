package com.auto.ingram.ui.navigation

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.getdriver.ui.order_form.MyUI
import com.example.getdriver.viewmodels.OrdersViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.service.market.R
import com.service.market.data.local.model.orders.Orders
import com.service.market.ui.auth.AuthScreen
import com.service.market.ui.auth.RegistrScreen
import com.service.market.ui.my_orders.MyOrderDetails
import com.service.market.ui.my_orders.MyOrderList
import com.service.market.ui.order.CheckboxExample
import com.service.market.ui.order.OrderDetails
import com.service.market.ui.profile.ProfileScreen
import com.service.market.viewmodels.MyOrderViewModel
import com.service.market.viewmodels.ServicesViewModel


@OptIn(ExperimentalPagingApi::class)
@Composable
fun MainWindows(){

    val viewModel = hiltViewModel<OrdersViewModel>()

    val servicesViewModel = hiltViewModel<ServicesViewModel>()

    val myOrderViewModel = hiltViewModel<MyOrderViewModel>()


    val navController = rememberNavController()

    val contextForToast = LocalContext.current.applicationContext

    val sharedPreference =  contextForToast.getSharedPreferences("account_info", Context.MODE_PRIVATE)
    val is_auth =  sharedPreference.getBoolean("is_auth",false)

    val lazyMovieItems: LazyPagingItems<Orders> = viewModel.orders.collectAsLazyPagingItems()


    var startDestination: String?


    if(is_auth == true){
        startDestination = BottomNavItem.RemontList.screen_route
    }
    else{
        startDestination = BottomNavItem.AuthForm.screen_route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,

    ) {
        composable(
            BottomNavItem.RemontList.screen_route
        ) {

            MainScreenView(viewModel, navController, servicesViewModel, myOrderViewModel, lazyMovieItems)

        }
        composable("add_form/service_id={service_id}&service_title={service_title}",
            arguments = listOf(navArgument("service_id") {
                type = NavType.IntType
            }, navArgument("service_title"){type = NavType.StringType})
        ) {
            MyUI(navController, it.arguments?.getInt("service_id") ?: 0, it.arguments?.getString("service_title") ?: "")

        }
        composable(BottomNavItem.RemontDetail.screen_route) {
                backStackEntry -> val posterId =
            backStackEntry.arguments?.getLong(NavScreen.PosterDetails.argument0) ?: return@composable
            OrderDetails(viewModel.clickedItem, navController)

        }
        composable(BottomNavItem.MyOrdersDetails.screen_route) {
                backStackEntry -> val posterId =
            backStackEntry.arguments?.getLong(NavScreen.PosterDetails.argument0) ?: return@composable

            MyOrderDetails(myOrderViewModel.clickedItem, navController)
        }
        composable(BottomNavItem.MyOrders.screen_route) {
            NetworkScreen(navController, servicesViewModel)
        }
        composable(BottomNavItem.CategoryFilter.screen_route) {
            CheckboxExample(servicesViewModel, navController)
        }
        composable(BottomNavItem.AuthForm.screen_route) {
            AuthScreen(navController)
        }
        composable(BottomNavItem.RegistrForm.screen_route) {
            RegistrScreen(navController)
        }
    }
}


@OptIn(ExperimentalPagingApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreenView(viewModel: OrdersViewModel, navController: NavController, servicesViewModel: ServicesViewModel, myOrderViewModel: MyOrderViewModel, lazyMovieItems: LazyPagingItems<Orders>){

    val contextForToast = LocalContext.current.applicationContext
    val sharedPreference =  contextForToast.getSharedPreferences("account_info", Context.MODE_PRIVATE)
    val is_client =  sharedPreference.getBoolean("is_client",false)


    lateinit var items : List<BottomNavItem>

    if(is_client == true) {
        items = listOf(
            BottomNavItem.Order,
            BottomNavItem.MyOrders,
            BottomNavItem.Profile
        )
    }
    else{
        items = listOf(
            BottomNavItem.Create,
            BottomNavItem.MyOrders,
            BottomNavItem.Profile
        )
    }

    val navController1 = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    Scaffold(
        drawerBackgroundColor = colorResource(id = R.color.purple_200),
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        },
        scaffoldState = scaffoldState,
        topBar = {
            if(is_client == false) {
                TopBar(scope = scope, scaffoldState = scaffoldState, navController = navController)
            }
            else{
                TopBarWithoutFilter(scope = scope, scaffoldState = scaffoldState, navController = navController)
            }

                 },
        bottomBar = { BottomNavigation(navController = navController1, items) },
        // Defaults to false
        isFloatingActionButtonDocked = false,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen
    ) {
            innerPadding ->
        Box(modifier = androidx.compose.ui.Modifier.padding(innerPadding)) {
            NavHost(navController1, startDestination = BottomNavItem.Order.screen_route) {
                composable(BottomNavItem.Order.screen_route) {

                    if(is_client == false) {
                        BlogScreen(navController, viewModel, servicesViewModel, lazyMovieItems)
                    }
                    else{
                        NetworkScreen(navController, servicesViewModel)
                    }

                }
                composable(BottomNavItem.Create.screen_route) {
                    if(is_client == false) {
                        BlogScreen(navController, viewModel, servicesViewModel, lazyMovieItems)
                    }
                    else{
                        NetworkScreen(navController, servicesViewModel)
                    }

                }
                composable(BottomNavItem.MyOrders.screen_route) {
                    MyOrderList(navController, myOrderViewModel)
                }
                composable(BottomNavItem.Profile.screen_route) {
                    ProfileScreen(navController)
                }
            }
        }
    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = colorResource(id = R.color.purple_200),
        contentColor = Color.White,
        actions = {
            IconButton(onClick = {
                navController.navigate(BottomNavItem.CategoryFilter.screen_route)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.filter_alt_24px),
                    contentDescription = null,
                )
            }
        }

    )
}



@Composable
fun TopBarWithoutFilter(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = colorResource(id = R.color.purple_200),
        contentColor = Color.White


    )
}





@Composable
fun BottomNavigation(navController: NavController, items: List<BottomNavItem>) {

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.orange),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.screen_route) },
                label = { Text(text = item.screen_route,
                    fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}




sealed class NavScreen(val route: String) {

    object Home : NavScreen("trendingMovieList")

    object PosterDetails : NavScreen("trendingMovieList") {

        const val routeWithArgument: String = "trendingMovieList/{posterId}"

        const val argument0: String = "posterId"
    }

}

