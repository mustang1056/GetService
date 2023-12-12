package com.auto.ingram.ui.navigation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.service.market.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {

    val contextForToast = LocalContext.current.applicationContext

    val sharedPreference = contextForToast.getSharedPreferences(
        "account_info",
        Context.MODE_PRIVATE
    )

    var text_btn by remember { mutableStateOf("") }

    val is_client =  sharedPreference.getBoolean("is_client",false)

    if(is_client == true){
        text_btn = "Исполнитель"
    }
    else{
        text_btn = "Клиент"
    }

    val items = listOf(
        BottomNavItem.Order
    )
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.purple_200))
    ) {
        // Header
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Test",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(10.dp)
        )
        // Space between
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )
        // List of navigation items
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.screen_route, onItemClick = {
                navController.navigate(item.screen_route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
                // Close drawer
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {


                    var editor = sharedPreference.edit()
                    if(is_client == true){
                        editor.putBoolean("is_client", false)
                        text_btn = "Исполнитель"
                    }
                    else{
                        editor.putBoolean("is_client", true)
                        text_btn = "Клиент"

                    }
                    editor.commit()

                    navController.navigate(BottomNavItem.RemontList.screen_route)

                          },
                shape = RoundedCornerShape(size = 22.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .width(180.dp)
                    .padding(top = 15.dp),

                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFFFF))

            ) {
                Text(
                    text = text_btn,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }

        Text(
            text = "Developed by Max Gerasimov",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DrawerItem(item: BottomNavItem, selected: Boolean, onItemClick: (BottomNavItem) -> Unit) {
    val background = if (selected) R.color.purple_200 else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(colorResource(id = background))
            .padding(start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = false)
@Composable
fun DrawerItemPreview() {
    DrawerItem(item = BottomNavItem.MyOrders, selected = false, onItemClick = {})
}


@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
}
