package com.service.market.ui.order_form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.service.market.data.local.model.service.Services
import com.service.market.viewmodels.ServicesViewModel


@Composable
fun CategoryList(navController: NavController, servicesViewModel: ServicesViewModel){

//val posters: List<Remont> by mainViewModel.posterList.collectAsState(initial = listOf())
    val lazyMovieItems by servicesViewModel.servicesVal.collectAsState(initial = listOf())

    CategoryList(navController = navController,movieList = lazyMovieItems,
        onItemClicked = servicesViewModel::itemClicked, servicesViewModel = servicesViewModel)


}

@Composable
fun CategoryList(
    navController: NavController,
    movieList:List<Services>,
    servicesViewModel: ServicesViewModel,
    onItemClicked:(item: Services) ->Unit
) {

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 16.dp),
        // New vertical spacing
        verticalArrangement = Arrangement.spacedBy(1.dp),

        ) {

        items(movieList) { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFF6650a4)
                    )
                },
                modifier = Modifier
                    .clickable {
                        val service_id = item.id
                        val service_title = item.title
                        navController.navigate("add_form/service_id=${service_id}&service_title=${service_title}")
                    }

            )

        }

    }

}